package org.snobotv2.examples.rev.subsystems;

//import com.kauailabs.navx.frc.AHRS;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SparkMaxPIDController;
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
    private static final double ENCODER_CONSTANT = (1.0 / DRIVETRAIN_CONSTANTS.getGearing()) * DRIVETRAIN_CONSTANTS.getkWheelDiameterMeters() * Math.PI;

    private final SimableCANSparkMax mLeadLeft; // NOPMD
    private final SimableCANSparkMax mFollowerLeft; // NOPMD

    private final SimableCANSparkMax mLeadRight; // NOPMD
    private final SimableCANSparkMax mFollowerRight; // NOPMD

    private final RelativeEncoder mRightEncoder;
    private final RelativeEncoder mLeftEncoder;

    private final SparkMaxPIDController mLeftPidController;
    private final SparkMaxPIDController mRightPidController;

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
        mLeadLeft = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerLeft = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_B, CANSparkMaxLowLevel.MotorType.kBrushless);
        mLeadRight = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerRight = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B, CANSparkMaxLowLevel.MotorType.kBrushless);

        mRightEncoder = mLeadRight.getEncoder();
        mLeftEncoder = mLeadLeft.getEncoder();

        mLeftEncoder.setPositionConversionFactor(ENCODER_CONSTANT);
        mRightEncoder.setPositionConversionFactor(ENCODER_CONSTANT);

        mLeftEncoder.setVelocityConversionFactor(ENCODER_CONSTANT / 60.0);
        mRightEncoder.setVelocityConversionFactor(ENCODER_CONSTANT / 60.0);

        mLeftPidController = mLeadLeft.getPIDController();
        mRightPidController = mLeadRight.getPIDController();

        mLeftEncoder.setPosition(0);
        mRightEncoder.setPosition(0);

        mLeadLeft.setInverted(false);
        mLeadRight.setInverted(true);

        mFollowerLeft.follow(mLeadLeft, false);
        mFollowerRight.follow(mLeadRight, false);

        mGyro = new AHRS();

        mDrive = new DifferentialDrive(mLeadLeft, mLeadRight);

        for (SparkMaxPIDController pidController : new SparkMaxPIDController[]{mLeftPidController, mRightPidController})
        {
            setupPidController(pidController, .02, 0, 0, 0.005_44, 144, 144);
        }

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(
                    DRIVETRAIN_CONSTANTS.createSim(),
                    new RevMotorControllerSimWrapper(mLeadLeft),
                    new RevMotorControllerSimWrapper(mLeadRight),
                    RevEncoderSimWrapper.create(mLeadLeft),
                    RevEncoderSimWrapper.create(mLeadRight),
                    new NavxWrapper().getYawGyro());
            mSimulator.setRightInverted(false);
            mSimulator.setLeftPdpChannels(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_LEFT_MOTOR_B_PDP);
            mSimulator.setRightPdpChannels(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B_PDP);
        }
    }

    private void setupPidController(SparkMaxPIDController pidController, double kp, double ki, double kd, double kff, double maxVelocity, double maxAcceleration)
    {
        pidController.setP(Units.metersToInches(kp));
        pidController.setI(ki);
        pidController.setD(kd);
        pidController.setFF(Units.metersToInches(kff));
        pidController.setSmartMotionMaxVelocity(Units.inchesToMeters(maxVelocity), 0);
        pidController.setSmartMotionMaxAccel(Units.inchesToMeters(maxAcceleration), 0);
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
        mLeftPidController.setReference(leftPosition, ControlType.kSmartMotion);
        mRightPidController.setReference(rightPosition, ControlType.kSmartMotion);
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
