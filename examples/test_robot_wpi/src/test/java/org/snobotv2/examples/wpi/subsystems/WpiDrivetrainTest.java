package org.snobotv2.examples.wpi.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("wpi")
public class WpiDrivetrainTest extends BaseUnitTest
{
    @Test
    public void testDriveForwards()
    {
        try (WpiDrivetrain drivetrainSubsystem = new WpiDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(1, 0);
                drivetrainSubsystem.simulationPeriodic();
            }

            assertTrue(drivetrainSubsystem.getLeftDistance() > 0);
            assertTrue(drivetrainSubsystem.getLeftRate() > 0);
            assertTrue(drivetrainSubsystem.getRightDistance() > 0);
            assertTrue(drivetrainSubsystem.getRightRate() > 0);
            assertEquals(drivetrainSubsystem.getHeadingDegrees(), 0, DEFAULT_EPSILON);
        }
    }

    @Test
    public void testDriveBackwards()
    {
        try (WpiDrivetrain drivetrainSubsystem = new WpiDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(-1, 0);
                drivetrainSubsystem.simulationPeriodic();
            }

            assertTrue(drivetrainSubsystem.getLeftDistance() < 0);
            assertTrue(drivetrainSubsystem.getLeftRate() < 0);
            assertTrue(drivetrainSubsystem.getRightDistance() < 0);
            assertTrue(drivetrainSubsystem.getRightRate() < 0);
            assertEquals(drivetrainSubsystem.getHeadingDegrees(), 0, DEFAULT_EPSILON);
        }
    }

    @Test
    public void testTurnClockwise()
    {
        try (WpiDrivetrain drivetrainSubsystem = new WpiDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, 1);
                drivetrainSubsystem.simulationPeriodic();
            }

            assertTrue(drivetrainSubsystem.getLeftDistance() > 0);
            assertTrue(drivetrainSubsystem.getLeftRate() > 0);
            assertTrue(drivetrainSubsystem.getRightDistance() < 0);
            assertTrue(drivetrainSubsystem.getRightRate() < 0);
            assertTrue(drivetrainSubsystem.getHeadingDegrees() > 0);
        }
    }

    @Test
    public void testTurnCounterClockwise()
    {
        try (WpiDrivetrain drivetrainSubsystem = new WpiDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, -1);
                drivetrainSubsystem.simulationPeriodic();
            }

            assertTrue(drivetrainSubsystem.getLeftDistance() < 0);
            assertTrue(drivetrainSubsystem.getLeftRate() < 0);
            assertTrue(drivetrainSubsystem.getRightDistance() > 0);
            assertTrue(drivetrainSubsystem.getRightRate() > 0);
            assertTrue(drivetrainSubsystem.getHeadingDegrees() < 0);
        }
    }
}
