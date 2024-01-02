package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkMax;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseRevlibUnitTest extends BaseUnitTest
{
    protected void testVoltagePercentage(double expected, CANSparkMax... motorControllers)
    {
        for (CANSparkMax motorController : motorControllers) // NOPMD
        {
            assertEquals(expected, motorController.getAppliedOutput(), DEFAULT_EPSILON, () -> "For " + motorController.getDeviceId());
        }
    }
}
