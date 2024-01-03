package org.snobotv2.module_wrappers.phoenix6;

import com.ctre.phoenix6.hardware.Pigeon2;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

@SuppressWarnings("removal")
public class Pigeon2Wrapper extends BaseGyroWrapper
{
    public Pigeon2Wrapper(Pigeon2 pigeon)
    {
        super((double angle) -> pigeon.getSimState().setRawYaw(-angle));
    }
}
