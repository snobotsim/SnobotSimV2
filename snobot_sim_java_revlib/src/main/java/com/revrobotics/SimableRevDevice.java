package com.revrobotics;

public interface SimableRevDevice
{
    int getDeviceId();

    double getAppliedOutput();

    void updateSim();
}
