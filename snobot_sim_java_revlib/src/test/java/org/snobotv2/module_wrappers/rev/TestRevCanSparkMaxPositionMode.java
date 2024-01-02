package org.snobotv2.module_wrappers.rev;

import org.junit.jupiter.api.Test;
import org.snobotv2.examples.rev.subsystems.RevElevatorSubsystem;
import org.snobotv2.test_utils.BaseUnitTest;

public class TestRevCanSparkMaxPositionMode extends BaseUnitTest
{
    @Test
    public void testGoToPosition()
    {
        try (RevElevatorSubsystem elevatorSubsystem = new RevElevatorSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                elevatorSubsystem.goToPosition(30);
                elevatorSubsystem.simulationPeriodic();
            }
        }

    }
}
