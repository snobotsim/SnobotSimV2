package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SimableCANSparkMax;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
@SuppressWarnings("PMD") // TODO temp
public class TestRevCanSparkMax extends BaseUnitTest
{
    @Test
    public void testBasic()
    {
        SimableCANSparkMax sparkMax = new SimableCANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
        SimableCANSparkMax follower = new SimableCANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
        follower.follow(sparkMax);

        CANPIDController pidController = sparkMax.getPIDController();

        pidController.setP(.5);
        assertEquals(.5, pidController.getP());

        RevMotorControllerSimWrapper talonWrapper = new RevMotorControllerSimWrapper(sparkMax);
        RevMotorControllerSimWrapper followerWrapper = new RevMotorControllerSimWrapper(follower);

        sparkMax.set(.4);
        talonWrapper.update();
        testVoltagePercentage(.4, talonWrapper, followerWrapper);
        testVoltagePercentage(.4, sparkMax, follower);

        sparkMax.set(-.25);
        talonWrapper.update();
        testVoltagePercentage(-.25, talonWrapper, followerWrapper);
        testVoltagePercentage(-.25, sparkMax, follower);

        follower.close();
        sparkMax.close();
    }
}
