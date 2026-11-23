package org.snobotv2.module_wrappers.phoenix6;

import org.wpilib.hardware.hal.HAL;
import org.snobotv2.test_utils.BaseUnitTest;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class BasePhoenix6UnitTest extends BaseUnitTest
{
    protected void ctreSimLoop()
    {
        HAL.simPeriodicBefore();
        HAL.simPeriodicAfter();

        try
        {
            Thread.sleep(20);
        } catch (InterruptedException e)
        {
            e.printStackTrace(); // NOPMD
        }
    }
}
