package org.snobotv2.examples.rev.subsystems;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("rev")
public class RevElevatorTest extends BaseUnitTest
{
    @Test
    public void testManuallyMoveUp()
    {
        try (RevElevatorSubsystem elevator = new RevElevatorSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
            }

            assertTrue(elevator.getHeightInches() > 0);
        }
    }

    @Test
    public void testManuallyMoveDown()
    {
        try (RevElevatorSubsystem elevator = new RevElevatorSubsystem())
        {
            // The sim won't let it go negative, so move up first
            for (int i = 0; i < 5; ++i)
            {
                elevator.moveManually(1);
                elevator.simulationPeriodic();
            }
            assertTrue(elevator.getHeightInches() > 0);

            for (int i = 0; i < 10; ++i)
            {
                elevator.moveManually(-1);
                elevator.simulationPeriodic();
            }
            assertEquals(elevator.getHeightInches(), 0);
        }
    }
}
