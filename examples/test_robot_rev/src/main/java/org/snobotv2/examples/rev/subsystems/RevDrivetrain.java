package org.snobotv2.examples.rev.subsystems;

//import com.kauailabs.navx.frc.AHRS;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.revrobotics.SimableCANSparkMax;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.util.Units;
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

    private final SimableCANSparkMax mMasterLeft; // NOPMD
    private final SimableCANSparkMax mFollowerLeft; // NOPMD

    private final SimableCANSparkMax mMasterRight; // NOPMD
    private final SimableCANSparkMax mFollowerRight; // NOPMD

    private final CANEncoder mRightEncoder;
    private final CANEncoder mLeftEncoder;

    private final CANPIDController mLeftPidController;
    private final CANPIDController mRightPidController;

    private final AHRS mGyro;

    private final DifferentialDrive mDrive;

    private DifferentialDrivetrainSimWrapper mSimulator;

    @Override
    public void close()
    {
        mMasterLeft.close();
        mFollowerLeft.close();
        mMasterRight.close();
        mFollowerRight.close();
    }

    public RevDrivetrain()
    {
        mMasterLeft = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerLeft = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_LEFT_MOTOR_B, CANSparkMaxLowLevel.MotorType.kBrushless);
        mMasterRight = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerRight = new SimableCANSparkMax(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B, CANSparkMaxLowLevel.MotorType.kBrushless);

        mRightEncoder = mMasterRight.getEncoder();
        mLeftEncoder = mMasterLeft.getEncoder();

        mLeftEncoder.setPositionConversionFactor(ENCODER_CONSTANT);
        mRightEncoder.setPositionConversionFactor(ENCODER_CONSTANT);

        mLeftEncoder.setVelocityConversionFactor(ENCODER_CONSTANT / 60.0);
        mRightEncoder.setVelocityConversionFactor(ENCODER_CONSTANT / 60.0);

        mLeftPidController = mMasterLeft.getPIDController();
        mRightPidController = mMasterRight.getPIDController();

        mLeftEncoder.setPosition(0);
        mRightEncoder.setPosition(0);

        mMasterLeft.setInverted(false);
        mMasterRight.setInverted(true);

        mFollowerLeft.follow(mMasterLeft, false);
        mFollowerRight.follow(mMasterRight, false);

        mGyro = new AHRS();

        mDrive = new DifferentialDrive(mMasterLeft, mMasterRight);
        mDrive.setRightSideInverted(false);

        for (CANPIDController pidController : new CANPIDController[]{mLeftPidController, mRightPidController})
        {
            setupPidController(pidController, .02, 0, 0, .00544, 144, 144);
        }

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(
                    DRIVETRAIN_CONSTANTS.createSim(),
                    new RevMotorControllerSimWrapper(mMasterLeft),
                    new RevMotorControllerSimWrapper(mMasterRight),
                    RevEncoderSimWrapper.create(mMasterLeft),
                    RevEncoderSimWrapper.create(mMasterRight),
                    new NavxWrapper().getYawGyro());
            mSimulator.setRightInverted(false);
            mSimulator.setLeftPdpChannels(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_LEFT_MOTOR_B_PDP);
            mSimulator.setRightPdpChannels(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B_PDP);
        }
    }

    private void setupPidController(CANPIDController pidController, double kp, double ki, double kd, double kff, double maxVelocity, double maxAcceleration)
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
        mMasterLeft.setVoltage(left);
        mMasterRight.setVoltage(right);
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
