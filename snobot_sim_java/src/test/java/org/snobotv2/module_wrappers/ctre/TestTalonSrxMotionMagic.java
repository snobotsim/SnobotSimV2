package org.snobotv2.module_wrappers.ctre;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.ctre.subsystems.CtreElevatorSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

@Tag("ctre")
public class TestTalonSrxMotionMagic extends BaseUnitTest
{
    @Test
    public void testMotionMagic()
    {
        try (CtreElevatorSubsystem elevatorSubsystem = new CtreElevatorSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                elevatorSubsystem.goToPositionMotionMagic(30);
                elevatorSubsystem.simulationPeriodic();
                ctreSimLoop();
            }
        }
    }
}
