package org.snobotv2.module_wrappers;

import org.snobotv2.interfaces.IMotorControllerWrapper;

import java.util.function.DoubleSupplier;

public class BaseMotorControllerWrapper implements IMotorControllerWrapper
{
    private final DoubleSupplier mVoltagePercentageGetter;
    private final int mId;

    protected BaseMotorControllerWrapper(int id, DoubleSupplier voltagePercentageGetter)
    {
        mId = id;
        mVoltagePercentageGetter = voltagePercentageGetter;
    }

    @Override
    public double getVoltagePercentage()
    {
        return mVoltagePercentageGetter.getAsDouble();
    }

    public int getId()
    {
        return mId;
    }
}
