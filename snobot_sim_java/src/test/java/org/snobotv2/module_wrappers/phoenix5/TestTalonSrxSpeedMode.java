package org.snobotv2.module_wrappers.phoenix5;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.phoenix5.subsystems.CtreShooterSubsystem;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

@Tag("ctre")
public class TestTalonSrxSpeedMode extends BaseUnitTest
{
    @Test
    public void testSpeedControl()
    {
        try (CtreShooterSubsystem shooterSubsystem = new CtreShooterSubsystem())
        {
            for (int i = 0; i < 5; ++i)
            {
                shooterSubsystem.spinAtRpm(2500);
                ctreSimLoop();
            }
        }


    }
}
