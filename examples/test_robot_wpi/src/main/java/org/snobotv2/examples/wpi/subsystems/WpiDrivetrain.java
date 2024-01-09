package org.snobotv2.examples.wpi.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.geometry.Pose2d;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.BaseDrivetrainSubsystem;
import org.snobotv2.module_wrappers.wpi.ADXRS450GyroWrapper;
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
    private final ADXRS450_Gyro mGyro;

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
        mGyro = new ADXRS450_Gyro();

        double distancePerPulse = DRIVETRAIN_CONSTANTS.getkWheelDiameterMeters() * Math.PI / 1024.;
        mLeftEncoder.setDistancePerPulse(distancePerPulse);
        mRightEncoder.setDistancePerPulse(distancePerPulse);

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(DRIVETRAIN_CONSTANTS.createSim(),
                    mLeftMotors, mDriveRightA,
                    mLeftEncoder, mRightEncoder,
                    new ADXRS450GyroWrapper(mGyro));
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
        mGyro.close();
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
        return mGyro.getAngle();
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
