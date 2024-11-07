package org.snobotv2.module_wrappers.phoenix5;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

@SuppressWarnings("removal")
public class CtrePigeonImuWrapper extends BaseGyroWrapper
{
    public CtrePigeonImuWrapper(WPI_PigeonIMU pigeon)
    {
        super((double angle) -> pigeon.getSimCollection().setRawHeading(-angle));
    }
}
