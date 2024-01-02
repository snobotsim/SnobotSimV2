package org.snobotv2.module_wrappers;

import org.snobotv2.interfaces.IEncoderWrapper;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class BaseEncoderWrapper implements IEncoderWrapper
{
    private final DoubleSupplier mPositionGetter;
    private final DoubleConsumer mPositionSetter;
    private final DoubleConsumer mVelocitySetter;

    protected BaseEncoderWrapper(
            DoubleSupplier positionGetter,
            DoubleConsumer positionSetter,
            DoubleConsumer velocitySetter)
    {
        mPositionGetter = positionGetter;
        mPositionSetter = positionSetter;
        mVelocitySetter = velocitySetter;
    }

    @Override
    public void setDistance(double distance)
    {
        mPositionSetter.accept(distance);
    }

    @Override
    public void setVelocity(double velocity)
    {
        mVelocitySetter.accept(velocity);
    }

    @Override
    public double getPosition()
    {
        return mPositionGetter.getAsDouble();
    }
}
