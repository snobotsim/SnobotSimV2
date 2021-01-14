package org.snobotv2.sim_wrappers;

import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;

public abstract class BaseSingleGearboxSimWrapper extends BaseSimWrapper
{
    protected final PdpSlots mPdpSlots;
    protected final IMotorControllerWrapper mMotor;
    protected final IEncoderWrapper mEncoderWrapper;

    public BaseSingleGearboxSimWrapper(IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper)
    {
        mMotor = motor;
        mEncoderWrapper = encoderWrapper;
        mPdpSlots = new PdpSlots();
    }

    public void setPdpChannels(Integer... channels)
    {
        mPdpSlots.setChannels(channels);
    }
}
