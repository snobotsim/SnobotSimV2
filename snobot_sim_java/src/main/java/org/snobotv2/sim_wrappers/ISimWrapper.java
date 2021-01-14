package org.snobotv2.sim_wrappers;

public interface ISimWrapper
{
    double DEFAULT_PERIOD = 0.02; // 20ms, 50hz

    void update();

    void setUpdatePeriod(double updatePeriod);
}
