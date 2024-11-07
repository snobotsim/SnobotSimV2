package org.snobotv2.module_wrappers.rev;

import com.revrobotics.spark.SparkBase;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class BaseRevlibUnitTest extends BaseUnitTest
{
    @SafeVarargs
    protected final <T extends SparkBase> void testVoltagePercentage(double expected, T... motorControllers)
    {
        for (T motorController : motorControllers) // NOPMD
        {
            assertEquals(expected, motorController.getAppliedOutput(), DEFAULT_EPSILON, () -> "For " + motorController.getDeviceId());
        }
    }
}
