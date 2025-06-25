package org.snobotv2.module_wrappers.navx;

import com.studica.frc.AHRS.NavXComType;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.snobotv2.SimDeviceDumpHelper;
import org.snobotv2.interfaces.IGyroWrapper;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

public class NavxWrapper
{
    private final IGyroWrapper mYawGyro;
    private final IGyroWrapper mPitchGyro;
    private final IGyroWrapper mRollGyro;

    public NavxWrapper(NavXComType comType)
    {
        SimDeviceDumpHelper.dumpSimDevices();
        int id = 0;
        switch (comType)
        {
        case kMXP_SPI:
            id = 4;
            break;
        case kMXP_UART, kI2C:
            id = 1;
            break;
        case kUSB1:
            id = 2;
            break;
        case kUSB2:
            id = 3;
            break;
        default:
            throw new UnsupportedOperationException();
        }
        SimDeviceSim deviceSim = new SimDeviceSim("navX-Sensor[" + id + "]");

        mYawGyro = new BaseGyroWrapper(deviceSim.getDouble("Yaw"));
        mPitchGyro = new BaseGyroWrapper(deviceSim.getDouble("Pitch"));
        mRollGyro = new BaseGyroWrapper(deviceSim.getDouble("Roll"));
    }

    public IGyroWrapper getYawGyro()
    {
        return mYawGyro;
    }

    public IGyroWrapper getPitchGyro()
    {
        return mPitchGyro;
    }

    public IGyroWrapper getRollGyro()
    {
        return mRollGyro;
    }
}
