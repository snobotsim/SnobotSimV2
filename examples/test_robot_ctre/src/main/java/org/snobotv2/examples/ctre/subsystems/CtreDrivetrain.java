package org.snobotv2.examples.ctre.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.BaseDrivetrainSubsystem;
import org.snobotv2.module_wrappers.ctre.CtreEncoderSimWrapper;
import org.snobotv2.module_wrappers.ctre.CtreMotorControllerSimWrapper;
import org.snobotv2.module_wrappers.wpi.ADXRS450GyroWrapper;
import org.snobotv2.sim_wrappers.DifferentialDrivetrainSimWrapper;

public class CtreDrivetrain extends BaseDrivetrainSubsystem
{
    private static final DrivetrainConstants DRIVETRAIN_CONSTANTS = new CimDrivetrainConstants();

    private static final double TICKS_TO_POSITION = 1.0 / 1024;
    private static final double RAW_TO_RPM = 1.0 / 100;

    private final WPI_TalonSRX mLeftLead;
    private final WPI_TalonSRX mRightLead;
    private final DifferentialDrive mDrive;

    private final ADXRS450_Gyro mGyro;

    private DifferentialDrivetrainSimWrapper mSimulator;

    public CtreDrivetrain()
    {
        mLeftLead = new WPI_TalonSRX(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A);
        mRightLead = new WPI_TalonSRX(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A);

        WPI_TalonSRX leftFollower = new WPI_TalonSRX(BaseConstants.DRIVETRAIN_LEFT_MOTOR_B);
        leftFollower.follow(mLeftLead);

        WPI_TalonSRX rightFollower = new WPI_TalonSRX(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B);
        rightFollower.follow(mRightLead);

        mDrive = new DifferentialDrive(mLeftLead, mRightLead);
        mDrive.setRightSideInverted(false);

        mGyro = new ADXRS450_Gyro();

        if (RobotBase.isSimulation())
        {
            mSimulator = new DifferentialDrivetrainSimWrapper(DRIVETRAIN_CONSTANTS.createSim(),
                    new CtreMotorControllerSimWrapper(mLeftLead),
                    new CtreMotorControllerSimWrapper(mRightLead),
                    new CtreEncoderSimWrapper(mLeftLead, TICKS_TO_POSITION),
                    new CtreEncoderSimWrapper(mRightLead, TICKS_TO_POSITION),
                    new ADXRS450GyroWrapper(mGyro));
            mSimulator.setRightInverted(false);
            mSimulator.setLeftPdpChannels(BaseConstants.DRIVETRAIN_LEFT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_LEFT_MOTOR_B_PDP);
            mSimulator.setRightPdpChannels(BaseConstants.DRIVETRAIN_RIGHT_MOTOR_A_PDP, BaseConstants.DRIVETRAIN_RIGHT_MOTOR_B_PDP);
        }
    }

    @Override
    public void close()
    {
        mLeftLead.free();
        mRightLead.free();
        mDrive.close();
        mGyro.close();
    }

    /////////////////////////////////////
    // Accessors
    /////////////////////////////////////
    @Override
    public double getLeftDistance()
    {
        return mLeftLead.getSelectedSensorPosition() * TICKS_TO_POSITION;
    }

    @Override
    public double getRightDistance()
    {
        return mRightLead.getSelectedSensorPosition() * TICKS_TO_POSITION;
    }

    @Override
    public double getLeftRate()
    {
        return mLeftLead.getSelectedSensorVelocity() * RAW_TO_RPM;
    }

    @Override
    public double getRightRate()
    {
        return mRightLead.getSelectedSensorVelocity() * RAW_TO_RPM;
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
        mLeftLead.set(left / RobotController.getBatteryVoltage());
        mRightLead.set(right / RobotController.getBatteryVoltage());
        mDrive.feed();
    }



    @Override
    public void smartVelocityControlMetersPerSec(double leftVelocityMetersPerSec, double rightVelocityMetersPerSec)
    {
        mLeftLead.set(ControlMode.Velocity, leftVelocityMetersPerSec);
        mRightLead.set(ControlMode.Velocity, rightVelocityMetersPerSec);
        mDrive.feed();
    }

    @Override
    public void driveDistance(double leftPosition, double rightPosition)
    {
        mLeftLead.set(ControlMode.Position, leftPosition);
        mRightLead.set(ControlMode.Position, rightPosition);
        mDrive.feed();
    }

    @Override
    public void resetEncoders()
    {
        mLeftLead.setSelectedSensorPosition(0);
        mRightLead.setSelectedSensorPosition(0);
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
