package org.snobotv2.test_utils;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.unmanaged.Unmanaged;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.simulation.SimulatorJNI;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD") // TODO temp
public class BaseUnitTest
{
    protected static final double DEFAULT_EPSILON = 1e-3;

    @BeforeEach
    public void setup()
    {
        // TODO move elsewhere
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        HAL.initialize(0, 0);
        SimulatorJNI.resetHandles();
        SimDeviceSim.resetData();
    }

    @AfterEach
    public void shutdown()
    {
        HAL.shutdown();
    }

    protected void ctreSimLoop()
    {
        HAL.simPeriodicBefore();
        Unmanaged.feedEnable(1000);
        HAL.simPeriodicAfter();

        try
        {
            Thread.sleep(150);
        } catch (InterruptedException e)
        {
            e.printStackTrace(); // NOPMD
        }
    }

    protected void testVoltagePercentage(double expected, BaseMotorControllerWrapper... wrappers)
    {
        for (BaseMotorControllerWrapper wrapper : wrappers)
        {
            assertEquals(expected, wrapper.getVoltagePercentage(), DEFAULT_EPSILON, () -> "For " + wrapper.getId());
        }
    }

    protected void testVoltagePercentage(double expected, WPI_TalonSRX... speedControllers)
    {
        for (WPI_TalonSRX speedController : speedControllers)
        {
            assertEquals(expected, speedController.getMotorOutputPercent(), DEFAULT_EPSILON, () -> "For " + speedController.getDeviceID());
        }
    }

    protected void testVoltagePercentage(double expected, CANSparkMax... speedControllers)
    {
        for (CANSparkMax speedController : speedControllers) // NOPMD
        {
            assertEquals(expected, speedController.getAppliedOutput(), DEFAULT_EPSILON, () -> "For " + speedController.getDeviceId());
            // This doesn't work for the simple SpeedController Interface unless you set it directly
            assertEquals(expected, speedController.get(), DEFAULT_EPSILON, () -> "For " + speedController.getDeviceId());
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
