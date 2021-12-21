package org.snobotv2;

import edu.wpi.first.hal.simulation.PowerDistributionDataJNI;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public final class SnobotSim
{
    private static final PowerDistribution PDP = new PowerDistribution();

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
