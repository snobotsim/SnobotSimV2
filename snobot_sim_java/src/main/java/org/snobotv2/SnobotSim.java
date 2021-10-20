package org.snobotv2;

import edu.wpi.first.hal.simulation.PDPDataJNI;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public final class SnobotSim
{
    private static final PowerDistributionPanel pdp = new PowerDistributionPanel();

    private SnobotSim()
    {
    }

    public static void update()
    {
        double[] currents = new double[16];
        for (int i = 0; i < 16; ++i)
        {
            currents[i] = pdp.getCurrent(i);
        }
        double batteryVoltage = BatterySim.calculateDefaultBatteryLoadedVoltage(currents);
        RoboRioSim.setVInVoltage(batteryVoltage);
        PDPDataJNI.setVoltage(0, batteryVoltage);
    }
}
