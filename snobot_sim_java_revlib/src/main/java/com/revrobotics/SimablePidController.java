package com.revrobotics;

import edu.wpi.first.wpilibj.RobotBase;

public class SimablePidController extends SparkPIDController
{
    protected final double[] mMaxAccel = new double[4];
    protected final double[] mMaxVel = new double[4];

    public SimablePidController(CANSparkBase device)
    {
        super(device);
    }

    // These don't seem to work with the provided sim

    @Override
    public REVLibError setSmartMotionMaxAccel(double maxAccel, int slotID)
    {
        if (RobotBase.isReal())
        {
            return super.setSmartMotionMaxAccel(maxAccel, slotID);
        }

        mMaxAccel[slotID] = maxAccel;
        return REVLibError.kOk;
    }

    @Override
    public REVLibError setSmartMotionMaxVelocity(double maxVel, int slotID)
    {
        if (RobotBase.isReal())
        {
            return super.setSmartMotionMaxVelocity(maxVel, slotID);
        }

        mMaxVel[slotID] = maxVel;
        return REVLibError.kOk;
    }

    @Override
    public double getSmartMotionMaxAccel(int slotID)
    {
        if (RobotBase.isReal())
        {
            return super.getSmartMotionMaxAccel(slotID);
        }
        return mMaxAccel[slotID];
    }

    @Override
    public double getSmartMotionMaxVelocity(int slotID)
    {
        if (RobotBase.isReal())
        {
            return super.getSmartMotionMaxVelocity(slotID);
        }
        return mMaxVel[slotID];
    }
}
