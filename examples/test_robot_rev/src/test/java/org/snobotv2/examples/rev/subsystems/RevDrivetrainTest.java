package org.snobotv2.examples.rev.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class RevDrivetrainTest extends BaseUnitTest
{
    private static final long NAVX_BOOT_TIME_MS = 50 + 10;
    private static final long NAVX_LOOP_TIME_MS = 20 + 1;
    private static final int LOOPS_TO_TEST = 10;

    @Test
    public void testDriveForwards() throws InterruptedException
    {
        try (RevDrivetrain drivetrainSubsystem = new RevDrivetrain())
        {
            Thread.sleep(NAVX_BOOT_TIME_MS); // Sleep for navx

            for (int i = 0; i < LOOPS_TO_TEST; ++i)
            {
                drivetrainSubsystem.arcadeDrive(1, 0);
                drivetrainSubsystem.simulationPeriodic();
                Thread.sleep(NAVX_LOOP_TIME_MS);
            }

            assertPositive(drivetrainSubsystem.getLeftDistance());
            assertPositive(drivetrainSubsystem.getLeftRate());
            assertPositive(drivetrainSubsystem.getRightDistance());
            assertPositive(drivetrainSubsystem.getRightRate());
            assertEquals(drivetrainSubsystem.getHeadingDegrees(), 0, DEFAULT_EPSILON);
        }
    }

    @Test
    public void testDriveBackwards() throws InterruptedException
    {
        try (RevDrivetrain drivetrainSubsystem = new RevDrivetrain())
        {
            Thread.sleep(NAVX_BOOT_TIME_MS); // Sleep for navx

            for (int i = 0; i < LOOPS_TO_TEST; ++i)
            {
                drivetrainSubsystem.arcadeDrive(-1, 0);
                drivetrainSubsystem.simulationPeriodic();
                Thread.sleep(NAVX_LOOP_TIME_MS);
            }

            assertNegative(drivetrainSubsystem.getLeftDistance());
            assertNegative(drivetrainSubsystem.getLeftRate());
            assertNegative(drivetrainSubsystem.getRightDistance());
            assertNegative(drivetrainSubsystem.getRightRate());
            assertEquals(drivetrainSubsystem.getHeadingDegrees(), 0, DEFAULT_EPSILON);
        }
    }

    @Test
    public void testTurnClockwise() throws InterruptedException
    {
        try (RevDrivetrain drivetrainSubsystem = new RevDrivetrain())
        {
            Thread.sleep(NAVX_BOOT_TIME_MS); // Sleep for navx

            for (int i = 0; i < LOOPS_TO_TEST; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, -.5);
                drivetrainSubsystem.simulationPeriodic();
                drivetrainSubsystem.getHeadingDegrees();
                Thread.sleep(NAVX_LOOP_TIME_MS);
            }

            assertPositive(drivetrainSubsystem.getLeftDistance());
            assertPositive(drivetrainSubsystem.getLeftRate());
            assertNegative(drivetrainSubsystem.getRightDistance());
            assertNegative(drivetrainSubsystem.getRightRate());
            assertPositive(drivetrainSubsystem.getHeadingDegrees());
        }
    }

    @Test
    public void testTurnCounterClockwise() throws InterruptedException
    {
        try (RevDrivetrain drivetrainSubsystem = new RevDrivetrain())
        {
            Thread.sleep(NAVX_BOOT_TIME_MS); // Sleep for navx

            for (int i = 0; i < LOOPS_TO_TEST; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, .5);
                drivetrainSubsystem.simulationPeriodic();
                drivetrainSubsystem.getHeadingDegrees();
                Thread.sleep(NAVX_LOOP_TIME_MS);
            }

            assertNegative(drivetrainSubsystem.getLeftDistance());
            assertNegative(drivetrainSubsystem.getLeftRate());
            assertPositive(drivetrainSubsystem.getRightDistance());
            assertPositive(drivetrainSubsystem.getRightRate());
            assertNegative(drivetrainSubsystem.getHeadingDegrees());
        }
    }
}
