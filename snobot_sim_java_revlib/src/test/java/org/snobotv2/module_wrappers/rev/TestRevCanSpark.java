package org.snobotv2.module_wrappers.rev;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfigAccessor;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class TestRevCanSpark extends BaseRevlibUnitTest
{
    @Test
    public void testBasicMax()
    {
        try (SparkMax sparkMax = new SparkMax(1, SparkBase.MotorType.kBrushless);
             SparkMax follower = new SparkMax(11, SparkBase.MotorType.kBrushless))
        {
            runTest(sparkMax, follower, new SparkMaxConfig(), sparkMax.configAccessor);
        }
    }

    @Test
    public void testBasicFlex()
    {
        try (SparkFlex sparkFlex = new SparkFlex(1, SparkBase.MotorType.kBrushless);
             SparkFlex follower = new SparkFlex(11, SparkBase.MotorType.kBrushless))
        {
            runTest(sparkFlex, follower, new SparkFlexConfig(), sparkFlex.configAccessor);
        }
    }

    @SuppressWarnings({"PMD.GenericsNaming", "PMD.UnusedFormalParameter"})
    private <MotorType extends SparkBase, ConfigType extends SparkBaseConfig, ConfigAccessor extends SparkBaseConfigAccessor> void runTest(
            MotorType sparkMax, MotorType follower, ConfigType config, ConfigAccessor configAccessor)
    {
        config.closedLoop.p(.4);
        config.closedLoop.i(.5);
        config.closedLoop.d(.6);
        config.closedLoop.feedForward.kV(.7);
        config.closedLoop.maxMotion.maxAcceleration(.8);
        config.closedLoop.maxMotion.cruiseVelocity(.9);
        sparkMax.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        follower.configure(config.follow(sparkMax), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        assertEquals(.4, configAccessor.closedLoop.getP(), DEFAULT_EPSILON);
        assertEquals(.5, configAccessor.closedLoop.getI(), DEFAULT_EPSILON);
        assertEquals(.6, configAccessor.closedLoop.getD(), DEFAULT_EPSILON);
        assertEquals(.7, configAccessor.closedLoop.feedForward.getkV(), DEFAULT_EPSILON);
        assertEquals(.8, configAccessor.closedLoop.maxMotion.getMaxAcceleration(), DEFAULT_EPSILON);
        assertEquals(.9, configAccessor.closedLoop.maxMotion.getCruiseVelocity(), DEFAULT_EPSILON);

        DCMotor gearbox = DCMotor.getNEO(1);
        RevMotorControllerSimWrapper leaderWrapper = new RevMotorControllerSimWrapper(sparkMax, gearbox);
        RevMotorControllerSimWrapper followerWrapper = new RevMotorControllerSimWrapper(follower, gearbox);

        sparkMax.set(.4);
        leaderWrapper.update();
        followerWrapper.update();
        testVoltagePercentage(.4, leaderWrapper);
        testVoltagePercentage(.4, sparkMax);

        sparkMax.set(-.25);
        leaderWrapper.update();
        testVoltagePercentage(-.25, leaderWrapper);
        testVoltagePercentage(-.25, sparkMax);
    }
}
