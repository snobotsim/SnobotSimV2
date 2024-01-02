package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class TestRevCanSparkMax extends BaseRevlibUnitTest
{
    @Test
    public void testBasic()
    {
        try (SimableCANSparkMax sparkMax = new SimableCANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
             SimableCANSparkMax follower = new SimableCANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless))
        {
            follower.follow(sparkMax);

            SparkMaxPIDController pidController = sparkMax.getPIDController();

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
}
