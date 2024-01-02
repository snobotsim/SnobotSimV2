package org.snobotv2.sim_wrappers;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;
import org.snobotv2.examples.wpi.subsystems.WpiShooterSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("flaky")
public class FlywheelSimWrapperTest extends BaseUnitTest
{
    @Test
    public void testFlywheel() throws Exception
    {
        try (ShooterSubsystem shooterSubsystem = new WpiShooterSubsystem())
        {
            for (int i = 0; i < 100; ++i)
            {
                shooterSubsystem.spinAtRpm(3500);
                shooterSubsystem.simulationPeriodic();
            }
            assertEquals(3500, shooterSubsystem.getRPM(), 1.5);
        }
    }
}
