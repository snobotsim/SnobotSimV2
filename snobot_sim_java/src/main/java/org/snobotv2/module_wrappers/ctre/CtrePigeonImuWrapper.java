package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import com.ctre.phoenix.unmanaged.Unmanaged;
import edu.wpi.first.hal.HAL;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

public class CtrePigeonImuWrapper extends BaseGyroWrapper
{
    public CtrePigeonImuWrapper(WPI_PigeonIMU pigeon)
    {
        super((double angle) -> pigeon.getSimCollection().setRawHeading(-angle));
        pigeon.getSimCollection().setRawHeading(0.0);

        HAL.simPeriodicBefore();
        Unmanaged.feedEnable(1000);
        HAL.simPeriodicAfter();

        try
        {
            Thread.sleep(250);
        } catch (InterruptedException e)
        {
            e.printStackTrace(); // NOPMD
        }
    }

    public CtrePigeonImuWrapper(WPI_Pigeon2 pigeon)
    {
        super((double angle) -> pigeon.getSimCollection().setRawHeading(-angle));
        pigeon.getSimCollection().setRawHeading(0.0);

        HAL.simPeriodicBefore();
        Unmanaged.feedEnable(1000);
        HAL.simPeriodicAfter();

        try
        {
            Thread.sleep(250);
        } catch (InterruptedException e)
        {
            e.printStackTrace(); // NOPMD
        }
    }
}
