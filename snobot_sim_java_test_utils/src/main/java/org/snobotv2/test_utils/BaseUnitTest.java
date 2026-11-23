package org.snobotv2.test_utils;

import org.wpilib.hardware.hal.HAL;
import org.wpilib.hardware.hal.simulation.SimulatorJNI;
import org.wpilib.simulation.SimDeviceSim;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class BaseUnitTest
{
    protected static final double DEFAULT_EPSILON = 1e-3;

    @BeforeEach
    public void setup()
    {
        // TODO move elsewhere
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        HAL.initialize();
        SimulatorJNI.resetHandles();
        SimDeviceSim.resetData();
    }

    @AfterEach
    public void shutdown()
    {
        HAL.shutdown();
    }

    protected void testVoltagePercentage(double expected, BaseMotorControllerWrapper... wrappers)
    {
        for (BaseMotorControllerWrapper wrapper : wrappers)
        {
            assertEquals(expected, wrapper.getVoltagePercentage(), DEFAULT_EPSILON, () -> "For " + wrapper.getId());
        }
    }

    protected void assertPositive(double value)
    {
        assertTrue(value > 0, "Expected positive, got " + value);
    }

    protected void assertNegative(double value)
    {
        assertTrue(value < 0, "Expected negative, got " + value);
    }
}
