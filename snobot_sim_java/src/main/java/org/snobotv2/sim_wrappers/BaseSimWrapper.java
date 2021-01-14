package org.snobotv2.sim_wrappers;

public abstract class BaseSimWrapper implements ISimWrapper
{
    protected double mUpdatePeriod;
    protected final int mPdpModule;

    public BaseSimWrapper()
    {
        mUpdatePeriod = DEFAULT_PERIOD;
        mPdpModule = 0;
    }

    @Override
    public void setUpdatePeriod(double updatePeriod)
    {
        mUpdatePeriod = updatePeriod;
    }
}
