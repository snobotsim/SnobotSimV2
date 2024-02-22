package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.SimableCANSparkFlex;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SimableRevDevice;
import com.revrobotics.SparkPIDController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class TestRevCanSpark extends BaseRevlibUnitTest
{
    @Test
    public void testBasicMax()
    {
        try (SimableCANSparkMax sparkMax = new SimableCANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
             SimableCANSparkMax follower = new SimableCANSparkMax(11, CANSparkLowLevel.MotorType.kBrushless))
        {
            runTest(sparkMax, follower);
        }
    }

    @Test
    public void testBasicFlex()
    {
        try (SimableCANSparkFlex sparkFlex = new SimableCANSparkFlex(1, CANSparkLowLevel.MotorType.kBrushless);
             SimableCANSparkFlex follower = new SimableCANSparkFlex(11, CANSparkLowLevel.MotorType.kBrushless))
        {
            runTest(sparkFlex, follower);
        }
    }

    private <T extends CANSparkBase & SimableRevDevice> void runTest(T sparkMax, T follower)
    {
        follower.follow(sparkMax);

        SparkPIDController pidController = sparkMax.getPIDController();

        pidController.setP(.4);
        pidController.setI(.5);
        pidController.setD(.6);
        pidController.setFF(.7);
        pidController.setSmartMotionMaxAccel(.8, 0);
        pidController.setSmartMotionMaxVelocity(.9, 0);

        assertEquals(.4, pidController.getP(), DEFAULT_EPSILON);
        assertEquals(.5, pidController.getI(), DEFAULT_EPSILON);
        assertEquals(.6, pidController.getD(), DEFAULT_EPSILON);
        assertEquals(.7, pidController.getFF(), DEFAULT_EPSILON);
        assertEquals(.8, pidController.getSmartMotionMaxAccel(0), DEFAULT_EPSILON);
        assertEquals(.9, pidController.getSmartMotionMaxVelocity(0), DEFAULT_EPSILON);

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
    }
}
