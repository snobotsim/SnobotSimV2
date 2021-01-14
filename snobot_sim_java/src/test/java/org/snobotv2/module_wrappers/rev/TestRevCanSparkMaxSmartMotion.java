package org.snobotv2.module_wrappers.rev;

import org.junit.jupiter.api.Tag;
import org.snobotv2.examples.rev.subsystems.RevDrivetrain;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
public class TestRevCanSparkMaxSmartMotion extends BaseUnitTest
{
    @Test
    public void testDrivetrain()
    {
        try (RevDrivetrain drivetrain = new RevDrivetrain())
        {

            double goal = 16.5;

//        System.out.println("Starting");
            for (int i = 0; i < 300; ++i)
            {
                drivetrain.driveDistance(goal, goal);
                drivetrain.simulationPeriodic();
//                System.out.println("Loop " + i);

            }

            assertEquals(goal, drivetrain.getRightDistance(), .02);
            assertEquals(goal, drivetrain.getLeftDistance(), .02);
            assertEquals(0, drivetrain.getRightRate(), .2);
            assertEquals(0, drivetrain.getLeftRate(), .2);
            assertEquals(0, drivetrain.getPositionError(goal, goal), .02);
        }
    }

}
