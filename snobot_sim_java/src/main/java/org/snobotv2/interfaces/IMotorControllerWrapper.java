package org.snobotv2.interfaces;

public interface IMotorControllerWrapper
{
    double getVoltagePercentage();

    default void update()
    {
        // Nothing to do for default case
    }

    default void setCurrent(double current)
    {
        // Nothing to do for default case
    }
}
