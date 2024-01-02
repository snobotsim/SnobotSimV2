package org.snobotv2.module_wrappers.phoenix5;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.phoenix5.subsystems.CtreElevatorSubsystem;
import org.junit.jupiter.api.Test;

@Tag("ctre")
public class TestTalonSrxPositionMode extends BasePhoenix5UnitTest
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
