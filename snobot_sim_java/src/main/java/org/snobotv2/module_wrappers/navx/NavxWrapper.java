package org.snobotv2.module_wrappers.navx;

import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.snobotv2.interfaces.IGyroWrapper;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

public class NavxWrapper
{
    private final IGyroWrapper mYawGyro;
    private final IGyroWrapper mPitchGyro;
    private final IGyroWrapper mRollGyro;

    public NavxWrapper()
    {
        SimDeviceSim deviceSim = new SimDeviceSim("navX-Sensor[0]");

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
