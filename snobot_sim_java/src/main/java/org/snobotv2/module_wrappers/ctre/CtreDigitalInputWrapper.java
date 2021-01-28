package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.snobotv2.module_wrappers.BaseDigitalInputWrapper;

import java.util.function.Consumer;

public class CtreDigitalInputWrapper extends BaseDigitalInputWrapper
{
    private static Consumer<Boolean> getConsumer(WPI_TalonSRX talonSrx, boolean isFwd)
    {
        if (isFwd)
        {
            return state -> talonSrx.getSimCollection().setLimitFwd(!state);
        }
        return state -> talonSrx.getSimCollection().setLimitRev(!state);
    }

    public CtreDigitalInputWrapper(WPI_TalonSRX talonSrx, boolean fwd)
    {
        super(getConsumer(talonSrx, fwd));
    }
}
