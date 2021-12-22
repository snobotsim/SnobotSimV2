package org.snobotv2.sim_wrappers;

import edu.wpi.first.hal.simulation.PowerDistributionDataJNI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdpSlots
{
    private final List<Integer> mPdpSlots;

    public PdpSlots()
    {
        mPdpSlots = new ArrayList<>();
    }

    public void update(int pdpModule, double current)
    {
        for (int pdpChannel : mPdpSlots)
        {
            PowerDistributionDataJNI.setCurrent(pdpModule, pdpChannel, current);
        }
    }

    public void setChannels(Integer... channels)
    {
        mPdpSlots.addAll(Arrays.asList(channels));
    }
}
