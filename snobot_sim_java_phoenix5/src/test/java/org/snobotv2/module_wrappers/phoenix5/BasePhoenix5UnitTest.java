package org.snobotv2.module_wrappers.phoenix5;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.unmanaged.Unmanaged;
import edu.wpi.first.hal.HAL;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @SuppressWarnings("PMD.CloseResource")
    protected void testVoltagePercentage(double expected, WPI_TalonSRX... motorControllers)
    {
        for (WPI_TalonSRX motorController : motorControllers)
        {
            assertEquals(expected, motorController.getMotorOutputPercent(), DEFAULT_EPSILON, () -> "For " + motorController.getDeviceID());
        }
    }

}
