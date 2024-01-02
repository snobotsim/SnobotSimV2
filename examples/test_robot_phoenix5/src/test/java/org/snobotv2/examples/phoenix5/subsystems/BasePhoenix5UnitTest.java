package org.snobotv2.examples.phoenix5.subsystems;

import com.ctre.phoenix.unmanaged.Unmanaged;
import edu.wpi.first.hal.HAL;
import org.snobotv2.test_utils.BaseUnitTest;


public class BasePhoenix5UnitTest extends BaseUnitTest
{
    protected void ctreSimLoop()
    {
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
