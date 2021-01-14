package org.snobotv2.examples.wpi.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("wpi")
public class WpiElevatorTest extends BaseUnitTest
{
    @Test
    public void testManuallyMoveUp()
    {
        try (WpiElevatorSubsystem elevator = new WpiElevatorSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
            }
            assertPositive(elevator.getHeightInches());
        }
    }

    @Test
    public void testManuallyMoveDown()
    {
        try (WpiElevatorSubsystem elevator = new WpiElevatorSubsystem())
        {
            // The sim won't let it go negative, so move up first
            for (int i = 0; i < 5; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
            }
            assertPositive(elevator.getHeightInches());

            for (int i = 0; i < 10; ++i)
            {
                elevator.moveManually(-1);
                elevator.simulationPeriodic();
            }
            assertEquals(elevator.getHeightInches(), 0, DEFAULT_EPSILON);
        }
    }
}
