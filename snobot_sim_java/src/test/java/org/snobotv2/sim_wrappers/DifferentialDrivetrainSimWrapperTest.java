package org.snobotv2.sim_wrappers;

import org.snobotv2.examples.wpi.subsystems.WpiDrivetrain;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifferentialDrivetrainSimWrapperTest extends BaseUnitTest
{
    @Test
    public void testDriveStraight()
    {
        try (WpiDrivetrain drivetrainSim = new WpiDrivetrain())
        {
            // It takes about 2 seconds to get up to speed
            for (int i = 0; i <= 500; ++i)
            {
                drivetrainSim.arcadeDrive(1, 0);
                drivetrainSim.simulationPeriodic();

//            System.out.println(i + " - " + drivetrainSim.mLeftEncoder.getRate() + ", " + drivetrainSim.mRightEncoder.getRate() + ", " + drivetrainSim.mGyro.getAngle());
            }
//        System.out.println(drivetrainSim.getLeftRate() + ", " + drivetrainSim.getRightRate() + ", " + drivetrainSim.getHeadingDegrees());
//        System.out.println(drivetrainSim.getLeftDistance() + ", " + drivetrainSim.getRightDistance() + ", " + drivetrainSim.getHeadingDegrees());

            assertEquals(6.0606, drivetrainSim.getLeftRate(), 1e-2);
            assertEquals(6.0606, drivetrainSim.getRightRate(), 1e-2);
            assertEquals(0, drivetrainSim.getHeadingDegrees(), 1e-2);
        }
    }

    @Test
    public void turnRight()
    {
        try (WpiDrivetrain drivetrainSim = new WpiDrivetrain())
        {
            // It takes about 2 seconds to get up to speed
            for (int i = 0; i < 100; ++i)
            {
                drivetrainSim.arcadeDrive(0, -1);
                drivetrainSim.simulationPeriodic();
//            System.out.println(i + " - " + drivetrainSim.mLeftEncoder.getDistance() + ", " + drivetrainSim.mRightEncoder.getDistance() + ", " + drivetrainSim.mGyro.getAngle());
            }

            assertEquals(8, drivetrainSim.getLeftRate(), 1e-2);
            assertEquals(-8, drivetrainSim.getRightRate(), 1e-2);
            assertTrue(drivetrainSim.getHeadingDegrees() > 0);
        }
    }
}
