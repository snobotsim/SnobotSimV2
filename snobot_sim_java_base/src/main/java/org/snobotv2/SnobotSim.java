package org.snobotv2;

import org.wpilib.hardware.hal.simulation.PowerDistributionDataJNI;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.power.PowerDistribution;
import org.wpilib.simulation.BatterySim;
import org.wpilib.simulation.RoboRioSim;

public final class SnobotSim
{
    private static final PowerDistribution PDP = new PowerDistribution(CANPort.CAN_S0);

    private SnobotSim()
    {
    }

    public static void update()
    {
        double[] currents = new double[16];
        for (int i = 0; i < 16; ++i)
        {
            currents[i] = PDP.getCurrent(i);
        }
        double batteryVoltage = BatterySim.calculateDefaultBatteryLoadedVoltage(currents);
        RoboRioSim.setVInVoltage(batteryVoltage);
        PowerDistributionDataJNI.setVoltage(0, batteryVoltage);
    }
}
