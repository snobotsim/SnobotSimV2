package org.snobotv2.examples.rev.subsystems;

import org.wpilib.hardware.bus.CANPort;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkLowLevel.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.util.Units;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.framework.RobotBase;
import org.wpilib.drive.DifferentialDrive;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.BaseDrivetrainSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.module_wrappers.wpi.OnboardIMUWrapper;
import org.snobotv2.sim_wrappers.DifferentialDrivetrainSimWrapper;

public class RevDrivetrain extends BaseDrivetrainSubsystem
{
    private static final DrivetrainConstants DRIVETRAIN_CONSTANTS = new NeoDrivetrainConstants();
    // REVLib 2027 removed on-device conversion factors, so the encoders report rotations and RPM.
    // These convert native units to meters and meters/second.
    private static final double METERS_PER_ROTATION = 1.0 / DRIVETRAIN_CONSTANTS.getGearing() * DRIVETRAIN_CONSTANTS.getkWheelDiameterMeters() * Math.PI;
    private static final double METERS_PER_SECOND_PER_RPM = METERS_PER_ROTATION / 60.0;
    private static final ClosedLoopSlot VELOCITY_SLOT = ClosedLoopSlot.kSlot0;
    private static final ClosedLoopSlot POSITION_SLOT = ClosedLoopSlot.kSlot1;

    private final SparkMax mLeadLeft; // NOPMD
    private final SparkMax mFollowerLeft; // NOPMD

    private final SparkMax mLeadRight; // NOPMD
    private final SparkMax mFollowerRight; // NOPMD

    private final RelativeEncoder mRightEncoder;
    private final RelativeEncoder mLeftEncoder;

    private final SparkClosedLoopController mLeftPidController;
    private final SparkClosedLoopController mRightPidController;

    private final OnboardIMU mGyro;

    private final DifferentialDrive mDrive;

    private DifferentialDrivetrainSimWrapper mSimulator;

    @Override
    public void close()
    {
        mLeadLeft.close();
        mFollowerLeft.close();
        mLeadRight.close();
        mFollowerRight.close();
    }

    public RevDrivetrain()
    {
        SparkMaxConfig commonConfig = new SparkMaxConfig();
        commonConfig.idleMode(IdleMode.kCoast);
        commonConfig.smartCurrentLimit(80);
        // The gains were tuned in meters, so scale them into native units
        commonConfig.closedLoop.pid(.03 * METERS_PER_SECOND_PER_RPM, 0, 0, VELOCITY_SLOT);
        commonConfig.closedLoop.feedForward.kV(0.21 * METERS_PER_SECOND_PER_RPM, VELOCITY_SLOT);
        commonConfig.closedLoop.pid(.03 * METERS_PER_ROTATION, 0, 0, POSITION_SLOT);
        commonConfig.closedLoop.feedForward.kV(0.21 * METERS_PER_ROTATION, POSITION_SLOT);
        commonConfig.closedLoop.maxMotion.cruiseVelocity(Units.inchesToMeters(144) / METERS_PER_ROTATION, POSITION_SLOT);
        commonConfig.closedLoop.maxMotion.maxAcceleration(Units.inchesToMeters(144) / METERS_PER_ROTATION, POSITION_SLOT);

        mLeadLeft = new SparkMax(CANPort.CAN_S0, BaseConstants.DRIVETRAIN_LEFT_MOTOR_A, MotorType.kBrushless);
        mFollowerLeft = new SparkMax(CANPort.CAN_S0, BaseConstants.DRIVETRAIN_LEFT_MOTOR_B, MotorType.kBrushless);
        mLeadRight = new SparkMax(CANPort.CAN_S0, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A, MotorType.kBrushless);
        mFollowerRight = new SparkMax(CANPort.CAN_S0, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B, MotorType.kBrushless);

        ResetMode resetMode = ResetMode.kResetSafeParameters;
        PersistMode persistMode = PersistMode.kPersistParameters;

        mLeadLeft.configure(
                new SparkMaxConfig()
                        .apply(commonConfig)
                        .inverted(false),
                resetMode, persistMode);
        mFollowerLeft.configure(
                new SparkMaxConfig()
                    .apply(commonConfig)
                    .inverted(false)
                    .follow(mLeadLeft),
                resetMode, persistMode);
        mLeadRight.configure(
                new SparkMaxConfig()
                        .inverted(true)
                        .apply(commonConfig),
                resetMode, persistMode);
        mFollowerRight.configure(
                new SparkMaxConfig()
                        .apply(commonConfig)
                        .inverted(true)
                        .follow(mLeadRight),
                resetMode, persistMode);

        mRightEncoder = mLeadRight.getEncoder();
        mLeftEncoder = mLeadLeft.getEncoder();

        mLeftPidController = mLeadLeft.getClosedLoopController();
        mRightPidController = mLeadRight.getClosedLoopController();

        mLeftEncoder.setPosition(0);
        mRightEncoder.setPosition(0);

        mGyro = new OnboardIMU(OnboardIMU.MountOrientation.FLAT);

        mDrive = new DifferentialDrive(mLeadLeft, mLeadRight);

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(
                    DRIVETRAIN_CONSTANTS.createSim(),
                    new RevMotorControllerSimWrapper(mLeadLeft, DRIVETRAIN_CONSTANTS.getMotor()),
                    new RevMotorControllerSimWrapper(mLeadRight, DRIVETRAIN_CONSTANTS.getMotor()),
                    RevEncoderSimWrapper.create(mLeadLeft, METERS_PER_ROTATION, METERS_PER_SECOND_PER_RPM),
                    RevEncoderSimWrapper.create(mLeadRight, METERS_PER_ROTATION, METERS_PER_SECOND_PER_RPM),
                    new OnboardIMUWrapper(mGyro));
            mSimulator.setRightInverted(false);
            mSimulator.setLeftPdpChannels(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_LEFT_MOTOR_B_PDP);
            mSimulator.setRightPdpChannels(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B_PDP);
        }
    }

    /////////////////////////////////////
    // Accessors
    /////////////////////////////////////
    @Override
    public double getLeftDistance()
    {
        return mLeftEncoder.getPosition().get() * METERS_PER_ROTATION;
    }

    @Override
    public double getRightDistance()
    {
        return mRightEncoder.getPosition().get() * METERS_PER_ROTATION;
    }

    @Override
    public double getLeftRate()
    {
        return mLeftEncoder.getVelocity().get() * METERS_PER_SECOND_PER_RPM;
    }

    @Override
    public double getRightRate()
    {
        return mRightEncoder.getVelocity().get() * METERS_PER_SECOND_PER_RPM;
    }

    @Override
    public double getHeadingDegrees()
    {
        return -Math.toDegrees(mGyro.getYawRadians());
    }

    @Override
    public DrivetrainConstants getConstants()
    {
        return DRIVETRAIN_CONSTANTS;
    }

    /////////////////////////////////////
    // Control
    /////////////////////////////////////
    @Override
    public void arcadeDrive(double speed, double rotation)
    {
        mDrive.arcadeDrive(speed, rotation);
    }

    @Override
    public void tankDriveVolts(double left, double right)
    {
        mLeadLeft.setVoltage(left);
        mLeadRight.setVoltage(right);
        mDrive.feed();
    }

    @Override
    public void smartVelocityControlMetersPerSec(double leftVelocityMetersPerSec, double rightVelocityMetersPerSec)
    {
        mLeftPidController.setSetpoint(leftVelocityMetersPerSec / METERS_PER_SECOND_PER_RPM, ControlType.kVelocity, VELOCITY_SLOT);
        mRightPidController.setSetpoint(rightVelocityMetersPerSec / METERS_PER_SECOND_PER_RPM, ControlType.kVelocity, VELOCITY_SLOT);
        mDrive.feed();
    }

    @Override
    public void driveDistance(double leftPosition, double rightPosition)
    {
        mLeftPidController.setSetpoint(leftPosition / METERS_PER_ROTATION, ControlType.kMAXMotionPositionControl, POSITION_SLOT);
        mRightPidController.setSetpoint(rightPosition / METERS_PER_ROTATION, ControlType.kMAXMotionPositionControl, POSITION_SLOT);
        mDrive.feed();
    }

    @Override
    public void resetEncoders()
    {
        mLeftEncoder.setPosition(0);
        mRightEncoder.setPosition(0);
    }

    @Override
    protected void resetSimOdometry(Pose2d pose)
    {
        mSimulator.resetOdometry(pose);
    }

    ///////////////////////////////
    // Life Cycle
    ///////////////////////////////
    @Override
    public void periodic()
    {
        updateOdometry();
    }

    @Override
    public void simulationPeriodic()
    {
        mSimulator.update();
    }
}
