// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.snobotv2.examples.phoenix5_swerve.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.swerve.SwerveModuleSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.snobotv2.examples.base_swerve.SwerveModuleOptimizationUtils;
import org.snobotv2.examples.base_swerve.subsystems.BaseSwerveModule;
import org.snobotv2.examples.phoenix5_swerve.CtreUtils;
import org.snobotv2.module_wrappers.phoenix5.CtreEncoderSimWrapper;
import org.snobotv2.module_wrappers.phoenix5.CtreMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.SwerveModuleSimWrapper;

@SuppressWarnings("removal")
public class SwerveModule implements BaseSwerveModule
{
    private static final double DEFAULT_TFF = 0.0;
    private static final double DEFAULT_TP = 0.1;

    private static final double DEFAULT_DFF = 0.0;
    private static final double DEFAULT_DP = 0;

    private final String mName;

    private final WPI_TalonSRX mDriveMotor;
    private final WPI_TalonSRX mAzimuthMotor;
    private final WPI_CANCoder mAbsoluteEncoder;

    private SwerveModuleState mCurrentState;
    private SwerveModuleState mDesiredState;
    private SwerveModulePosition mCurrentPosition = new SwerveModulePosition();
    private double mTurnGoalTicks;
    private double mDriveGoalTicks;

    private SwerveModuleSimWrapper mSimWrapper;

    public SwerveModule(
            int driveMotorChannel,
            int turningMotorChannel,
            int turningEncoderChannels,
            double encoderOffset,
            String name)
    {
        mDriveMotor = new WPI_TalonSRX(driveMotorChannel);
        mAzimuthMotor = new WPI_TalonSRX(turningMotorChannel);

        mAbsoluteEncoder = new WPI_CANCoder(turningEncoderChannels);

        // Set the distance (in this case, angle) per pulse for the turning encoder.
        // This is the angle through an entire rotation (2 * pi) divided by the
        // encoder resolution.
        mAbsoluteEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);
        mAbsoluteEncoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);
        mAbsoluteEncoder.configMagnetOffset(encoderOffset);
        mAbsoluteEncoder.configSensorDirection(false);
        mAbsoluteEncoder.setPositionToAbsolute();

        mAzimuthMotor.setInverted(true);

        mDriveMotor.configVoltageCompSaturation(10);
        mDriveMotor.configClosedloopRamp(0.5);
        mDriveMotor.configOpenloopRamp(0.5);

        SmartDashboard.putNumber("DFF", DEFAULT_DFF);
        SmartDashboard.putNumber("DP", DEFAULT_DP);

        SmartDashboard.putNumber("TFF", DEFAULT_TFF);
        SmartDashboard.putNumber("TP", DEFAULT_TP);

        mDesiredState = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
        mCurrentState = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
        mName = name;

        if (RobotBase.isSimulation())
        {
            SwerveModuleSim moduleSim = new SwerveModuleSim(
                    DCMotor.getNEO(1),
                    DCMotor.getNEO(1),
                    DriveSubsystem.kWheelDiameterMeters / 2,
                    TURNING_GEAR_RATION,
                    DRIVE_GEAR_RATION
            );
            mSimWrapper = new SwerveModuleSimWrapper(
                    moduleSim,
                    new CtreMotorControllerSimWrapper(mDriveMotor),
                    new CtreMotorControllerSimWrapper(mAzimuthMotor),
                    new CtreEncoderSimWrapper(mDriveMotor, CtreUtils.falconToDegrees(1, DRIVE_GEAR_RATION) / 360 / DriveSubsystem.kWheelCircumfranceMeters),
                    new CtreEncoderSimWrapper(mAzimuthMotor, CtreUtils.falconToDegrees(1, TURNING_GEAR_RATION)),
                    DriveSubsystem.kWheelDiameterMeters * Math.PI);
        }
    }

    @Override
    public void close()
    {
        mAbsoluteEncoder.close();
        mDriveMotor.close();
        mAzimuthMotor.close();
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    @Override
    public SwerveModuleState getState()
    {
        return mCurrentState;
    }

    @Override
    public SwerveModulePosition getPosition()
    {
        return mCurrentPosition;
    }

    public Rotation2d getCancoderCurrentAngle()
    {
        return new Rotation2d(Units.degreesToRadians(mAbsoluteEncoder.getAbsolutePosition()));
    }

    public Rotation2d getTurningMotorAngle()
    {
        return Rotation2d.fromDegrees(CtreUtils.falconToDegrees(mAzimuthMotor.getSelectedSensorPosition(), TURNING_GEAR_RATION));
    }

    public double getDriveSpeedMps()
    {
        return CtreUtils.falconToMps(mDriveMotor.getSelectedSensorVelocity(), DriveSubsystem.kWheelCircumfranceMeters, DRIVE_GEAR_RATION);

    }

    @Override
    public SwerveModuleState getDesiredState()
    {
        return mDesiredState;
    }

    /**
     * Sets the desired state for the module.
     *
     * @param inputState Desired state with speed and angle.
     */
    @Override
    public void setDesiredState(SwerveModuleState inputState, boolean openLoopDriving)
    {
        // Optimize the reference state to avoid spinning further than 90 degrees
        mDesiredState = SwerveModuleOptimizationUtils.optimize(inputState, getState().angle);

        mDriveGoalTicks = CtreUtils.mpsToFalcon(mDesiredState.speedMetersPerSecond, DriveSubsystem.kWheelCircumfranceMeters, DRIVE_GEAR_RATION);
        mTurnGoalTicks = CtreUtils.degreesToFalcon(mDesiredState.angle.getDegrees(), TURNING_GEAR_RATION);

        if (openLoopDriving)
        {
            mDriveMotor.set(mDesiredState.speedMetersPerSecond / DriveSubsystem.kMaxSpeedMetersPerSecond);
        } else
        {
            mDriveMotor.set(ControlMode.Velocity, mDriveGoalTicks);
        }
        mAzimuthMotor.set(ControlMode.Position, mTurnGoalTicks);

    }

    @Override
    public double getDriveMotorPercentage()
    {
        return mDriveMotor.getMotorOutputPercent();
    }

    @Override
    public double getTurningMotorPercentage()
    {
        return mAzimuthMotor.getMotorOutputPercent();
    }

    /**
     * Zeroes all the SwerveModule encoders.
     */
    @Override
    public void resetEncoders()
    {
        mDriveMotor.setSelectedSensorPosition(0);
        mAbsoluteEncoder.setPosition(0);
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addDoubleProperty("CancoderAngle", () -> getCancoderCurrentAngle().getDegrees(), null);
        builder.addDoubleProperty("MotorAngle", () -> getTurningMotorAngle().getDegrees(), null);

        builder.addDoubleProperty("TurnTicks", mAzimuthMotor::getSelectedSensorPosition, null);
        builder.addDoubleProperty("TurnGoalDegrees", mDesiredState.angle::getDegrees, null);
        builder.addDoubleProperty("TurnGoalTicks", () -> mTurnGoalTicks, null);

        builder.addDoubleProperty("DriveVelTicks", mDriveMotor::getSelectedSensorVelocity, null);
        builder.addDoubleProperty("DriveGoalMps", () -> mDesiredState.speedMetersPerSecond, null);
        builder.addDoubleProperty("DriveGoalTicks", () -> mDriveGoalTicks, null);
    }


    @Override
    public void periodic()
    {
        mDriveMotor.config_kF(0, SmartDashboard.getNumber("DFF", DEFAULT_DFF));
        mDriveMotor.config_kP(0, SmartDashboard.getNumber("DP", DEFAULT_DP));


        mAzimuthMotor.config_kF(0, SmartDashboard.getNumber("TFF", DEFAULT_TFF));
        mAzimuthMotor.config_kP(0, SmartDashboard.getNumber("TP", DEFAULT_TP));


        mCurrentState = new SwerveModuleState(
                getDriveSpeedMps(),
                getTurningMotorAngle());

        mCurrentPosition = new SwerveModulePosition(
                getDriveDistance(),
                getTurningMotorAngle()
        );
    }

    private double getDriveDistance()
    {
        return mDriveMotor.getSelectedSensorPosition();
    }

    @Override
    public SwerveModuleSimWrapper getSimWrapper()
    {
        return mSimWrapper;
    }

    @Override
    public void stop()
    {
        mAzimuthMotor.stopMotor();
        mDriveMotor.stopMotor();
    }

    @Override
    public void setOpenLoop(double turnSpeed, double driveSpeed)
    {
        mAzimuthMotor.set(ControlMode.PercentOutput, turnSpeed);
        mDriveMotor.set(ControlMode.PercentOutput, driveSpeed);
    }
}
