package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IGyroWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiMotorControllerWrapper;

public class DifferentialDrivetrainSimWrapper extends BaseSimWrapper
{
    private final IMotorControllerWrapper mLeftMotor;
    private final IMotorControllerWrapper mRightMotor;
    private final IEncoderWrapper mLeftEncoderSim;
    private final IEncoderWrapper mRightEncoderSim;
    private final IGyroWrapper mGyroWrapper;
    private final DifferentialDrivetrainSim mDrivetrainSim;

    private final PdpSlots mLeftPdpSlots;
    private final PdpSlots mRightPdpSlots;

    private double mRightMultiplier;

    public DifferentialDrivetrainSimWrapper(
        DifferentialDrivetrainSim drivetrainSim,
        MotorController leftMotor,
        MotorController rightMotor,
        Encoder leftEncoder,
        Encoder rightEncoder,
        IGyroWrapper gyro)
    {
        this(drivetrainSim,
            new WpiMotorControllerWrapper(leftMotor),
            new WpiMotorControllerWrapper(rightMotor),
            new WpiEncoderWrapper(leftEncoder),
            new WpiEncoderWrapper(rightEncoder),
            gyro);
    }

    public DifferentialDrivetrainSimWrapper(
        DifferentialDrivetrainSim drivetrainSim,
        IMotorControllerWrapper leftMotor,
        IMotorControllerWrapper rightMotor,
        IEncoderWrapper leftEncoderSim,
        IEncoderWrapper rightEncoderSim,
        IGyroWrapper gyroWrapper)
    {
        mDrivetrainSim = drivetrainSim;
        mLeftMotor = leftMotor;
        mRightMotor = rightMotor;
        mLeftEncoderSim = leftEncoderSim;
        mRightEncoderSim = rightEncoderSim;
        mGyroWrapper = gyroWrapper;

        mLeftPdpSlots = new PdpSlots();
        mRightPdpSlots = new PdpSlots();

        mRightMultiplier = -1;
    }

    public void setRightInverted(boolean inverted)
    {
        mRightMultiplier = inverted ? -1 : 1;
    }

    @Override
    public void update()
    {
        mLeftMotor.update();
        mRightMotor.update();

        mDrivetrainSim.setInputs(mLeftMotor.getVoltagePercentage() * RobotController.getBatteryVoltage(),
            mRightMultiplier * mRightMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());
        mDrivetrainSim.update(mUpdatePeriod);

        mLeftEncoderSim.setDistance(mDrivetrainSim.getLeftPositionMeters());
        mLeftEncoderSim.setVelocity(mDrivetrainSim.getLeftVelocityMetersPerSecond());
        mRightEncoderSim.setDistance(mDrivetrainSim.getRightPositionMeters());
        mRightEncoderSim.setVelocity(mDrivetrainSim.getRightVelocityMetersPerSecond());
        mGyroWrapper.setAngle(-mDrivetrainSim.getHeading().getDegrees());

        mLeftMotor.setCurrent(mDrivetrainSim.getLeftCurrentDrawAmps());
        mRightMotor.setCurrent(mDrivetrainSim.getRightCurrentDrawAmps());
        mLeftPdpSlots.update(mPdpModule, mDrivetrainSim.getLeftCurrentDrawAmps());
        mRightPdpSlots.update(mPdpModule, mDrivetrainSim.getRightCurrentDrawAmps());
    }

    public void resetOdometry(Pose2d pose)
    {
        mDrivetrainSim.setPose(pose);
    }

    public void setLeftPdpChannels(Integer... channels)
    {
        mLeftPdpSlots.setChannels(channels);
    }

    public void setRightPdpChannels(Integer... channels)
    {
        mRightPdpSlots.setChannels(channels);
    }

    public Pose2d getSimPose()
    {
        return mDrivetrainSim.getPose();
    }
}
