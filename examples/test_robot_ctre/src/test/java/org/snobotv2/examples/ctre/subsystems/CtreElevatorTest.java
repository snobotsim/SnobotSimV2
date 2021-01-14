package org.snobotv2.examples.ctre.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

@Tag("ctre")
public class CtreElevatorTest extends BaseUnitTest
{
    @Test
    public void testManuallyMoveUp() throws InterruptedException
    {
        Thread.sleep(50);
        try (CtreElevatorSubsystem elevator = new CtreElevatorSubsystem())
        {
            for (int i = 0; i < 25; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
                ctreSimLoop();
            }

            assertPositive(elevator.getHeightInches());
        }
    }

    @Test
    public void testManuallyMoveDown() throws InterruptedException
    {
        Thread.sleep(50);
        try (CtreElevatorSubsystem elevator = new CtreElevatorSubsystem())
        {
            // The sim won't let it go negative, so move up first
            for (int i = 0; i < 5; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
                ctreSimLoop();
            }
            assertPositive(elevator.getHeightInches());

            for (int i = 0; i < 20; ++i)
            {
                elevator.moveManually(-1);
                elevator.simulationPeriodic();
                ctreSimLoop();
            }
        }
    }
}
