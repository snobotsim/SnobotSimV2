package org.snobotv2.examples.phoenix5.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("ctre")
@Tag("flaky") // TODO CTRE is acting weird, likely because of the lack of determinism
public class CtreDrivetrainTest extends BasePhoenix5UnitTest
{
    @Test
    public void testDriveForwards() throws InterruptedException
    {
        try (CtreDrivetrain drivetrainSubsystem = new CtreDrivetrain())
        {
            Thread.sleep(50);
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(1, 0);
                drivetrainSubsystem.simulationPeriodic();
                ctreSimLoop();
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
        try (CtreDrivetrain drivetrainSubsystem = new CtreDrivetrain())
        {
            Thread.sleep(50);
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(-1, 0);
                drivetrainSubsystem.simulationPeriodic();
                ctreSimLoop();
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
        Thread.sleep(50);
        try (CtreDrivetrain drivetrainSubsystem = new CtreDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, 1);
                drivetrainSubsystem.simulationPeriodic();
                ctreSimLoop();
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
        Thread.sleep(50);
        try (CtreDrivetrain drivetrainSubsystem = new CtreDrivetrain())
        {
            for (int i = 0; i < 5; ++i)
            {
                drivetrainSubsystem.arcadeDrive(0, -1);
                drivetrainSubsystem.simulationPeriodic();
                ctreSimLoop();
            }

            assertNegative(drivetrainSubsystem.getLeftDistance());
            assertNegative(drivetrainSubsystem.getLeftRate());
            assertPositive(drivetrainSubsystem.getRightDistance());
            assertPositive(drivetrainSubsystem.getRightRate());
            assertNegative(drivetrainSubsystem.getHeadingDegrees());
        }
    }
}
