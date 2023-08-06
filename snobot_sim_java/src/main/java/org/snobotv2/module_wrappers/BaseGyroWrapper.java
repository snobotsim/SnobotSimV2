package org.snobotv2.module_wrappers;

import edu.wpi.first.hal.SimDouble;
import org.snobotv2.interfaces.IGyroWrapper;

import java.util.function.DoubleConsumer;

public class BaseGyroWrapper implements IGyroWrapper
{
    private final DoubleConsumer mSetter;

    public BaseGyroWrapper(SimDouble simDouble)
    {
        this(simDouble::set);
    }

    public BaseGyroWrapper(DoubleConsumer setter)
    {
        mSetter = setter;
        mSetter.accept(0.0);
    }


    @Override
    public void setAngle(double angleDegrees)
    {
        mSetter.accept(angleDegrees);
    }
}
