package org.snobotv2.examples.wpi.subsystems;

import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.hardware.motor.MotorController;
import org.wpilib.drivers.motor.PWMVictorSPX;
import org.wpilib.framework.RobotBase;
import org.wpilib.drive.DifferentialDrive;
import org.wpilib.math.geometry.Pose2d;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.BaseDrivetrainSubsystem;
import org.snobotv2.module_wrappers.wpi.OnboardIMUWrapper;
import org.snobotv2.sim_wrappers.DifferentialDrivetrainSimWrapper;

public class WpiDrivetrain extends BaseDrivetrainSubsystem
{
    private static final DrivetrainConstants DRIVETRAIN_CONSTANTS = new CimDrivetrainConstants();

    private final PWMVictorSPX mDriveLeftA;
    private final PWMVictorSPX mDriveLeftB;
    private final PWMVictorSPX mDriveRightA;
    private final PWMVictorSPX mDriveRightB;
    private final MotorController mLeftMotors;
    private final MotorController mRightMotors;
    private final DifferentialDrive mDrive;

    private final Encoder mLeftEncoder;
    private final Encoder mRightEncoder;
    private final OnboardIMU mGyro;

    private DifferentialDrivetrainSimWrapper mSimulator;

    public WpiDrivetrain()
    {
        mDriveLeftA = new PWMVictorSPX(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A);
        mDriveLeftB = new PWMVictorSPX(BaseConstants.DRIVETRAIN_LEFT_MOTOR_B);
        mDriveRightA = new PWMVictorSPX(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A);
        mDriveRightB = new PWMVictorSPX(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B);

        mLeftMotors = mDriveLeftA;
        mRightMotors = mDriveRightA;
        mRightMotors.setInverted(true);

        mDrive = new DifferentialDrive(mLeftMotors, mRightMotors);

        mLeftEncoder = new Encoder(BaseConstants.DRIVETRAIN_LEFT_ENCODER_A, BaseConstants.DRIVETRAIN_LEFT_ENCODER_B, false);
        mRightEncoder = new Encoder(BaseConstants.DRIVETRAIN_RIGHT_ENCODER_A, BaseConstants.DRIVETRAIN_RIGHT_ENCODER_B, true);
        mGyro = new OnboardIMU(OnboardIMU.MountOrientation.FLAT);

        double distancePerPulse = DRIVETRAIN_CONSTANTS.getkWheelDiameterMeters() * Math.PI / 1024.;
        mLeftEncoder.setDistancePerPulse(distancePerPulse);
        mRightEncoder.setDistancePerPulse(distancePerPulse);

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(DRIVETRAIN_CONSTANTS.createSim(),
                    mLeftMotors, mDriveRightA,
                    mLeftEncoder, mRightEncoder,
                    new OnboardIMUWrapper(mGyro));
        }
    }

    @Override
    public void close()
    {
        mDriveLeftA.close();
        mDriveLeftB.close();
        mDriveRightA.close();
        mDriveRightB.close();
        mLeftEncoder.close();
        mRightEncoder.close();
    }

    @Override
    public double getLeftDistance()
    {
        return mLeftEncoder.getDistance();
    }

    @Override
    public double getRightDistance()
    {
        return mRightEncoder.getDistance();
    }

    @Override
    public double getLeftRate()
    {
        return mLeftEncoder.getRate();
    }

    @Override
    public double getRightRate()
    {
        return mRightEncoder.getRate();
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
        mLeftMotors.setVoltage(left);
        mRightMotors.setVoltage(right);
        mDrive.feed();
    }

    @Override
    public void smartVelocityControlMetersPerSec(double leftVelocityMetersPerSec, double rightVelocityMetersPerSec)
    {
        throw new UnsupportedOperationException("WPI example does not support velocity control");
    }

    @Override
    public void driveDistance(double leftPosition, double rightPosition)
    {
        throw new UnsupportedOperationException("WPI example does not support distance control");
    }

    @Override
    public void resetEncoders()
    {
        mLeftEncoder.reset();
        mRightEncoder.reset();
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
