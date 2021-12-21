package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

public class CtrePigeonImuWrapper extends BaseGyroWrapper
{
    public CtrePigeonImuWrapper(WPI_PigeonIMU pigeon)
    {
        super((double angle) -> pigeon.getSimCollection().setRawHeading(-angle));
    }
}
