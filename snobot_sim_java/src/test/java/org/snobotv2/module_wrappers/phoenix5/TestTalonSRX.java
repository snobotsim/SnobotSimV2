package org.snobotv2.module_wrappers.phoenix5;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;


@Tag("ctre")
public class TestTalonSRX extends BaseUnitTest
{
    @Test
    public void testBasic()
    {
        try (WPI_TalonSRX talon = new WPI_TalonSRX(0);
             WPI_TalonSRX follower = new WPI_TalonSRX(1))
        {
            follower.follow(talon);

            for (int i = 0; i < 5; ++i)
            {
                talon.set(.4);
                ctreSimLoop();
            }

            CtreMotorControllerSimWrapper talonWrapper = new CtreMotorControllerSimWrapper(talon);
            CtreMotorControllerSimWrapper followerWrapper = new CtreMotorControllerSimWrapper(follower);

            talon.set(.4);
            ctreSimLoop();
            talonWrapper.update();
            testVoltagePercentage(.4, talon, follower);
            testVoltagePercentage(.4, talonWrapper, followerWrapper);

            talon.set(-.25);
            ctreSimLoop();
            talon.set(-.25);
            ctreSimLoop();
            talonWrapper.update();
            testVoltagePercentage(-.25, talon, follower);
            testVoltagePercentage(-.25, talonWrapper, followerWrapper);
        }
    }
}
