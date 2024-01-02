package org.snobotv2.sim_wrappers;

import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.examples.wpi.subsystems.WpiElevatorSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorSimWrapperTest extends BaseUnitTest
{
    @Test
    public void testElevatorSim()
    {
        try (WpiElevatorSubsystem elevatorSubsystem = new WpiElevatorSubsystem())
        {
            for (int i = 0; i < 100; ++i)
            {
                elevatorSubsystem.goToPosition(30);
                elevatorSubsystem.simulationPeriodic();
//            System.out.println("Elevator height " + elevatorSubsystem.getHeightInches());
            }

            assertEquals(30, elevatorSubsystem.getHeightInches(), ElevatorSubsystem.DEFAULT_ALLOWABLE_HEIGHT_ERROR_INCHES);
        }
    }
}
