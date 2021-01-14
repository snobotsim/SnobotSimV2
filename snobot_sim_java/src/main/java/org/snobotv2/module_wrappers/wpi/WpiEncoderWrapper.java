package org.snobotv2.module_wrappers.wpi;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import org.snobotv2.module_wrappers.BaseEncoderWrapper;

public class WpiEncoderWrapper extends BaseEncoderWrapper
{
    public WpiEncoderWrapper(Encoder encoder)
    {
        this(new EncoderSim(encoder));
    }

    public WpiEncoderWrapper(EncoderSim sim)
    {
        super(sim::getDistance, sim::setDistance, sim::setRate);
    }
}
