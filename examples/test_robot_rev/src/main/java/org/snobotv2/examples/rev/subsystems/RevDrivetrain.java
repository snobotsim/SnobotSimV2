package org.snobotv2.examples.rev.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.BaseDrivetrainSubsystem;
import org.snobotv2.module_wrappers.navx.NavxWrapper;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.DifferentialDrivetrainSimWrapper;

public class RevDrivetrain extends BaseDrivetrainSubsystem
{
    private static final DrivetrainConstants DRIVETRAIN_CONSTANTS = new NeoDrivetrainConstants();
    private static final double ENCODER_CONSTANT = 1.0 / DRIVETRAIN_CONSTANTS.getGearing() * DRIVETRAIN_CONSTANTS.getkWheelDiameterMeters() * Math.PI;

    private final SparkMax mLeadLeft; // NOPMD
    private final SparkMax mFollowerLeft; // NOPMD

    private final SparkMax mLeadRight; // NOPMD
    private final SparkMax mFollowerRight; // NOPMD

    private final RelativeEncoder mRightEncoder;
    private final RelativeEncoder mLeftEncoder;

    private final SparkClosedLoopController mLeftPidController;
    private final SparkClosedLoopController mRightPidController;

    private final AHRS mGyro;

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
        commonConfig.encoder.positionConversionFactor(ENCODER_CONSTANT);
        commonConfig.encoder.velocityConversionFactor(ENCODER_CONSTANT / 60.0);
        commonConfig.closedLoop.p(.03);
        commonConfig.closedLoop.i(0);
        commonConfig.closedLoop.d(0);
        commonConfig.closedLoop.velocityFF(0.21);
        commonConfig.closedLoop.maxMotion.maxVelocity(Units.inchesToMeters(144));
        commonConfig.closedLoop.maxMotion.maxAcceleration(Units.inchesToMeters(144));

        mLeadLeft = new SparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A, MotorType.kBrushless);
        mFollowerLeft = new SparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_B, MotorType.kBrushless);
        mLeadRight = new SparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A, MotorType.kBrushless);
        mFollowerRight = new SparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B, MotorType.kBrushless);

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

        mGyro = new AHRS(NavXComType.kMXP_SPI);

        mDrive = new DifferentialDrive(mLeadLeft, mLeadRight);

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(
                    DRIVETRAIN_CONSTANTS.createSim(),
                    new RevMotorControllerSimWrapper(mLeadLeft, DRIVETRAIN_CONSTANTS.getMotor()),
                    new RevMotorControllerSimWrapper(mLeadRight, DRIVETRAIN_CONSTANTS.getMotor()),
                    RevEncoderSimWrapper.create(mLeadLeft),
                    RevEncoderSimWrapper.create(mLeadRight),
                    new NavxWrapper().getYawGyro());
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
        return mLeftEncoder.getPosition();
    }

    @Override
    public double getRightDistance()
    {
        return mRightEncoder.getPosition();
    }

    @Override
    public double getLeftRate()
    {
        return mLeftEncoder.getVelocity();
    }

    @Override
    public double getRightRate()
    {
        return mRightEncoder.getVelocity();
    }

    @Override
    public double getHeadingDegrees()
    {
        return mGyro.getYaw();
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
        mLeftPidController.setReference(leftVelocityMetersPerSec, ControlType.kVelocity);
        mRightPidController.setReference(rightVelocityMetersPerSec, ControlType.kVelocity);
        mDrive.feed();
    }

    @Override
    public void driveDistance(double leftPosition, double rightPosition)
    {
        mLeftPidController.setReference(leftPosition, ControlType.kMAXMotionPositionControl);
        mRightPidController.setReference(rightPosition, ControlType.kMAXMotionPositionControl);
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
