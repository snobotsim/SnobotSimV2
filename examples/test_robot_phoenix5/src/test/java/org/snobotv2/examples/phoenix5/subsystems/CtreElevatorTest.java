package org.snobotv2.examples.phoenix5.subsystems;


import edu.wpi.first.math.util.Units;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ctre")
public class CtreElevatorTest extends BasePhoenix5UnitTest
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

            assertEquals(Units.metersToInches(ElevatorSubsystem.ElevatorSimConstants.kMaxElevatorHeight), elevator.getHeightInches(), .5);

            assertFalse(elevator.isAtLowerLimit());
            assertTrue(elevator.isAtUpperLimit());
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
            assertFalse(elevator.isAtLowerLimit());
            assertFalse(elevator.isAtUpperLimit());

            for (int i = 0; i < 20; ++i)
            {
                elevator.moveManually(-1);
                elevator.simulationPeriodic();
                ctreSimLoop();
            }

            assertTrue(elevator.isAtLowerLimit());
            assertFalse(elevator.isAtUpperLimit());
        }
    }
}
