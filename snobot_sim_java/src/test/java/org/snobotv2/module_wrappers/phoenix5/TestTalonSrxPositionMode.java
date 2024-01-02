package org.snobotv2.module_wrappers.phoenix5;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.phoenix5.subsystems.CtreElevatorSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

@Tag("ctre")
public class TestTalonSrxPositionMode extends BaseUnitTest
{
    @Test
    public void testGoToPosition()
    {
        try (CtreElevatorSubsystem elevatorSubsystem = new CtreElevatorSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                elevatorSubsystem.goToPosition(30);
                elevatorSubsystem.simulationPeriodic();
                ctreSimLoop();
            }
        }

    }
}
