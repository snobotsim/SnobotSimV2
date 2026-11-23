package org.snobotv2.module_wrappers.wpi;

import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.simulation.EncoderSim;
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
