package org.snobotv2.module_wrappers.rev;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.rev.subsystems.RevDrivetrain;
import org.snobotv2.examples.rev.subsystems.RevShooterSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class TestRevCanSparkMaxSpeedMode extends BaseUnitTest
{
    @Test
    public void testFlywheel()
    {
        try (RevShooterSubsystem shooterSubsystem = new RevShooterSubsystem())
        {
            for (int i = 0; i < 100; ++i)
            {
                shooterSubsystem.spinAtRpm(2500);
//            System.out.println(i + " - " + i * .02 + " - " + shooterSubsystem.getRPM());
                shooterSubsystem.simulationPeriodic();
            }
            assertEquals(2500, shooterSubsystem.getRPM(), 1.5);

            for (int i = 0; i < 100; ++i)
            {
                shooterSubsystem.spinAtRpm(3200);
//            System.out.println(i * .02 + " - " + shooterSubsystem.getRPM());
                shooterSubsystem.simulationPeriodic();
            }
            assertEquals(3200, shooterSubsystem.getRPM(), 1.5);
        }
    }

    @Test
    public void testDrivetrain()
    {
        try (RevDrivetrain drivetrain = new RevDrivetrain())
        {
            for (int i = 0; i < 50; ++i)
            {
                drivetrain.smartVelocityControlMetersPerSec(-2.1, -2.1);
//            System.out.println(i + " - "
//                + drivetrain.getLeftDistance() + ", " + drivetrain.getRightDistance() + ", "
//                + drivetrain.getLeftRate() + ", " + drivetrain.getRightRate());
                drivetrain.simulationPeriodic();
            }
            assertEquals(-2.1, drivetrain.getLeftRate(), .1);
            assertEquals(-2.1, drivetrain.getRightRate(), .1);

            for (int i = 0; i < 50; ++i)
            {
                drivetrain.smartVelocityControlMetersPerSec(4.3, 4.3);
//            System.out.println(i + " - "
//                + drivetrain.getLeftDistance() + ", " + drivetrain.getRightDistance() + ", "
//                + drivetrain.getLeftRate() + ", " + drivetrain.getRightRate());
                drivetrain.simulationPeriodic();
            }
            assertEquals(4.3, drivetrain.getLeftRate(), .1);
            assertEquals(4.3, drivetrain.getRightRate(), .1);
        }
    }
}
