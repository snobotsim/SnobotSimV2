// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.snobotv2.examples.rev_swerve.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.SparkMaxPIDController;
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
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.SwerveModuleSimWrapper;

public class SwerveModule implements BaseSwerveModule
{
    private static final double DRIVE_ENCODER_CONSTANT = (1.0 / DRIVE_GEAR_RATION) * DriveSubsystem.kWheelCircumfranceMeters;

    private static final double DEFAULT_FF = 0.34;
    private static final double DEFAULT_P = 0.001;

    private final String mName;

    private final AbsoluteEncoder mAbsoluteEncoder;

    private final SimableCANSparkMax mDriveMotor;
    private final SparkMaxPIDController mDriveController;
    private final RelativeEncoder mDriveEncoder;

    private final SimableCANSparkMax mAzimuthMotor;
    private final SparkMaxPIDController mAzimuthController;
    private final RelativeEncoder mAzimuthEncoder;

    private SwerveModuleState mCurrentState;
    private SwerveModuleState mDesiredState;
    private SwerveModulePosition mCurrentPosition;

    private SwerveModuleSimWrapper mSimWrapper;

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public SwerveModule(
            int driveMotorChannel,
            int turningMotorChannel,
            int turningEncoderChannels,
            double encoderOffset,
            String name)
    {
        mAzimuthMotor = new SimableCANSparkMax(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        mAzimuthMotor.setInverted(false);
        mAzimuthEncoder = mAzimuthMotor.getEncoder();
        mAzimuthEncoder.setPositionConversionFactor(360 / TURNING_GEAR_RATION);
        mAzimuthEncoder.setVelocityConversionFactor(360 / TURNING_GEAR_RATION / 60);
        mAzimuthController = mAzimuthMotor.getPIDController();
        mAzimuthController.setP(0.01);
        mAzimuthController.setD(0.0);

        mAbsoluteEncoder = mAzimuthMotor.getAbsoluteEncoder(SparkMaxAbsoluteEncoder.Type.kDutyCycle);

        mDriveMotor = new SimableCANSparkMax(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        mDriveMotor.setInverted(false);
        mDriveEncoder = mDriveMotor.getEncoder();
        mDriveEncoder.setPositionConversionFactor(DRIVE_ENCODER_CONSTANT);
        mDriveEncoder.setVelocityConversionFactor(DRIVE_ENCODER_CONSTANT / 60);
        mDriveController = mDriveMotor.getPIDController();

        mDesiredState = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
        mCurrentState = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
        mName = name;

        mCurrentPosition = new SwerveModulePosition(
                getDrivePosition(),
                getTurningMotorAngle());


        SmartDashboard.putNumber("FF", DEFAULT_FF);
        SmartDashboard.putNumber("P", DEFAULT_P);


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
                    new RevMotorControllerSimWrapper(mDriveMotor),
                    new RevMotorControllerSimWrapper(mAzimuthMotor),
                    RevEncoderSimWrapper.create(mDriveMotor),
                    RevEncoderSimWrapper.create(mAzimuthMotor),
                    DriveSubsystem.kWheelDiameterMeters * Math.PI);
        }
    }

    @Override
    public void close()
    {
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
        return new Rotation2d(Units.degreesToRadians(mAbsoluteEncoder.getPosition()));
    }

    public final Rotation2d getTurningMotorAngle()
    {
        return Rotation2d.fromDegrees(mAzimuthEncoder.getPosition());
    }

    public double getDriveSpeedMps()
    {
        return mDriveEncoder.getVelocity();

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

        if (openLoopDriving)
        {
            mDriveMotor.set(mDesiredState.speedMetersPerSecond / DriveSubsystem.kMaxSpeedMetersPerSecond);
        } else
        {
            mDriveController.setReference(mDesiredState.speedMetersPerSecond, CANSparkMax.ControlType.kVelocity);
        }
        mAzimuthController.setReference(mDesiredState.angle.getDegrees(), CANSparkMax.ControlType.kPosition);
    }

    @Override
    public double getDriveMotorPercentage()
    {
        return mDriveMotor.getAppliedOutput();
    }

    @Override
    public double getTurningMotorPercentage()
    {
        return mAzimuthMotor.getAppliedOutput();
    }

    /**
     * Zeroes all the SwerveModule encoders.
     */
    @Override
    public void resetEncoders()
    {
        mDriveEncoder.setPosition(0);
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

        builder.addDoubleProperty("TurnDegrees", mAzimuthEncoder::getPosition, null);
        builder.addDoubleProperty("TurnGoalDegrees", () -> mDesiredState.angle.getDegrees(), null);

        builder.addDoubleProperty("DriveMps", mDriveEncoder::getVelocity, null);
        builder.addDoubleProperty("DriveGoalMps", () -> mDesiredState.speedMetersPerSecond, null);
    }


    @Override
    public void periodic()
    {
        mDriveController.setFF(SmartDashboard.getNumber("FF", DEFAULT_FF));
        mDriveController.setP(SmartDashboard.getNumber("P", DEFAULT_P));

        mCurrentState = new SwerveModuleState(
                getDriveSpeedMps(),
                getTurningMotorAngle());
        mCurrentPosition = new SwerveModulePosition(
                getDrivePosition(),
                getTurningMotorAngle());
    }

    private double getDrivePosition()
    {
        return mDriveEncoder.getPosition();
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
        mAzimuthMotor.set(turnSpeed);
        mDriveMotor.set(driveSpeed);
    }
}
