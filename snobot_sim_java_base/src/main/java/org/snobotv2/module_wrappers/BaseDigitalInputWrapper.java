package org.snobotv2.module_wrappers;

import org.snobotv2.interfaces.IDigitalWrapper;

import java.util.function.Consumer;

public class BaseDigitalInputWrapper implements IDigitalWrapper
{
    private final Consumer<Boolean> mStateSetter;

    public BaseDigitalInputWrapper(Consumer<Boolean> stateSetter)
    {
        mStateSetter = stateSetter;
    }

    @Override
    public void set(boolean state)
    {
        mStateSetter.accept(state);
    }
}
