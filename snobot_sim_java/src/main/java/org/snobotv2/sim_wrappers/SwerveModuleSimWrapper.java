package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.swerve.SwerveModuleSim;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;

public class SwerveModuleSimWrapper extends BaseSimWrapper
{
    private final IMotorControllerWrapper mDriveMotor;
    private final IMotorControllerWrapper mTurnMotor;
    private final IEncoderWrapper mDriveEncoder;
    private final IEncoderWrapper mTurnEncoder;

    private final SwerveModuleSim mModuleSim;

    private final boolean mUseDegrees;

    public SwerveModuleSimWrapper(SwerveModuleSim moduleSim,
                                  IMotorControllerWrapper driveMotor,
                                  IMotorControllerWrapper turnMotor,
                                  IEncoderWrapper driveEncoder,
                                  IEncoderWrapper turnEncoder)
    {
        this(moduleSim, driveMotor, turnMotor, driveEncoder, turnEncoder, true);
    }

    public SwerveModuleSimWrapper(SwerveModuleSim moduleSim,
                                  IMotorControllerWrapper driveMotor,
                                  IMotorControllerWrapper turnMotor,
                                  IEncoderWrapper driveEncoder,
                                  IEncoderWrapper turnEncoder,
                                  boolean useDegrees)
    {
        mModuleSim = moduleSim;

        mDriveMotor = driveMotor;
        mTurnMotor = turnMotor;
        mDriveEncoder = driveEncoder;
        mTurnEncoder = turnEncoder;
        mUseDegrees = useDegrees;
    }

    @Override
    public void update()
    {
        mDriveMotor.update();
        mTurnMotor.update();

        mModuleSim.setInputVoltages(
                mDriveMotor.getVoltagePercentage() * RobotController.getBatteryVoltage(),
                mTurnMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());

        mDriveEncoder.setDistance(mModuleSim.getWheelEncoderPositionRev());
        mDriveEncoder.setVelocity(mModuleSim.getWheelEncoderMetersPerSecond());

        double azimuthPosition = mModuleSim.getAzimuthEncoderPositionRads();
        double azimuthVelocity = mModuleSim.getAzimuthEncoderVelocityRadPerSec();

        if (mUseDegrees)
        {
            azimuthPosition = Math.toDegrees(azimuthPosition);
            azimuthVelocity = Math.toDegrees(azimuthVelocity);
        }

        mTurnEncoder.setDistance(azimuthPosition);
        mTurnEncoder.setVelocity(azimuthVelocity);
    }

    public SwerveModuleSim getBaseSim()
    {
        return mModuleSim;
    }
}
