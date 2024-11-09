// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.snobotv2.examples.rev_swerve.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
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
    private static final double DRIVE_ENCODER_CONSTANT = 1.0 / DRIVE_GEAR_RATION * DriveSubsystem.kWheelCircumfranceMeters;

    private static final double DEFAULT_FF = 0.34;
    private static final double DEFAULT_P = 0.001;

    private final String mName;

    private final AbsoluteEncoder mAbsoluteEncoder;

    private final SparkMax mDriveMotor;
    private final SparkClosedLoopController mDriveController;
    private final RelativeEncoder mDriveEncoder;

    private final SparkMax mAzimuthMotor;
    private final SparkClosedLoopController mAzimuthController;
    private final RelativeEncoder mAzimuthEncoder;

    private SwerveModuleState mCurrentState;
    private SwerveModuleState mDesiredState;
    private SwerveModulePosition mCurrentPosition;

    private SwerveModuleSimWrapper mSimWrapper;

    private final SparkMaxConfig mAzimuthConfig;
    private final SparkMaxConfig mDriveConfig;

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public SwerveModule(
            int driveMotorChannel,
            int turningMotorChannel,
            int turningEncoderChannels,
            double encoderOffset,
            String name)
    {
        mAzimuthConfig = new SparkMaxConfig();
        mAzimuthConfig.inverted(false);
        mAzimuthConfig.encoder.positionConversionFactor(360 / TURNING_GEAR_RATION);
        mAzimuthConfig.encoder.velocityConversionFactor(360 / TURNING_GEAR_RATION / 60);
        mAzimuthConfig.closedLoop.p(0.01);
        mAzimuthConfig.closedLoop.d(0.0);

        mAzimuthMotor = new SparkMax(turningMotorChannel, SparkBase.MotorType.kBrushless);
        mAzimuthMotor.configure(mAzimuthConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
        mAzimuthMotor.setInverted(false);
        mAzimuthEncoder = mAzimuthMotor.getEncoder();
        mAzimuthController = mAzimuthMotor.getClosedLoopController();

        mAbsoluteEncoder = mAzimuthMotor.getAbsoluteEncoder();

        mDriveConfig = new SparkMaxConfig();
        mDriveConfig.inverted(false);
        mDriveConfig.encoder.positionConversionFactor(DRIVE_ENCODER_CONSTANT);
        mDriveConfig.encoder.velocityConversionFactor(DRIVE_ENCODER_CONSTANT / 60);
        mDriveConfig.closedLoop.p(0.01);
        mDriveConfig.closedLoop.d(0.0);

        mDriveMotor = new SparkMax(driveMotorChannel, SparkBase.MotorType.kBrushless);
        mDriveMotor.configure(mDriveConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
        mDriveEncoder = mDriveMotor.getEncoder();
        mDriveController = mDriveMotor.getClosedLoopController();

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
                    new RevMotorControllerSimWrapper(mDriveMotor, DCMotor.getNEO(1)),
                    new RevMotorControllerSimWrapper(mAzimuthMotor, DCMotor.getNEO(1)),
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
            mDriveController.setReference(mDesiredState.speedMetersPerSecond, SparkBase.ControlType.kVelocity);
        }
        mAzimuthController.setReference(mDesiredState.angle.getDegrees(), SparkBase.ControlType.kPosition);
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
        builder.addDoubleProperty("TurnGoalDegrees", mDesiredState.angle::getDegrees, null);

        builder.addDoubleProperty("DriveMps", mDriveEncoder::getVelocity, null);
        builder.addDoubleProperty("DriveGoalMps", () -> mDesiredState.speedMetersPerSecond, null);
    }


    @Override
    public void periodic()
    {
        mDriveConfig.closedLoop.velocityFF(SmartDashboard.getNumber("FF", DEFAULT_FF));
        mDriveConfig.closedLoop.p(SmartDashboard.getNumber("P", DEFAULT_P));
        mDriveMotor.configure(mDriveConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

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
