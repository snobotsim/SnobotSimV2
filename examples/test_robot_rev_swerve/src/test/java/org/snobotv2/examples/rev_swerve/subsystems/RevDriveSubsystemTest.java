package org.snobotv2.examples.rev_swerve.subsystems;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("rev")
@SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
public class RevDriveSubsystemTest extends BaseUnitTest
{
    private static final int LOOPS_TO_TEST = 50;
    private static final double VELOCITY_TOLERANCE = 1e-6;
    private static final double POSITION_TOLERANCE = 1e-6;
    private static final double ANGLE_TOLERANCE = 1e-6;

    private void runLoop(DriveSubsystem drive, double x, double y, double rot)
    {
        drive.periodic();
        drive.simulationPeriodic();
        drive.teleDrive(x, y, rot);
    }

    private void runLoops(DriveSubsystem drive, double x, double y, double rot)
    {
        for (int i = 0; i < LOOPS_TO_TEST; ++i)
        {
            runLoop(drive, x, y, rot);
        }
    }

    private void assertModuleState(SwerveModuleState state, double expectedSpeed, double expectedDegrees)
    {
        assertEquals(expectedSpeed, state.speedMetersPerSecond, VELOCITY_TOLERANCE);
        assertEquals(expectedDegrees, state.angle.getDegrees());
    }

    @Test
    public void testDriveForwards()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, DriveSubsystem.kMaxSpeedMetersPerSecond, 0, 0);

            assertEquals(2.218769509611857, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(2.218769509611857, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);
            for (SwerveModuleState state : drive.getModuleStates())
            {
                assertModuleState(state, 2.9068093299865723, 0);
            }
        }
    }

    @Test
    public void testDriveBackwards()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, -DriveSubsystem.kMaxSpeedMetersPerSecond, 0, 0);

            assertEquals(2.218769509611857, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(-2.218769509611857, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);
            for (SwerveModuleState state : drive.getModuleStates())
            {
                assertModuleState(state,  -2.9068093299865723, 0);
            }
        }
    }


    @Test
    public void testDriveLeft()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, 0, DriveSubsystem.kMaxSpeedMetersPerSecond, 0);

            assertEquals(2.180822825435927, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(0.10008672844112536, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(2.1785249235967656, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);
            for (SwerveModuleState state : drive.getModuleStates())
            {
                assertModuleState(state,  2.9046378135681152, 90);
            }
        }
    }

    @Test
    public void testDriveRight()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, 0, -DriveSubsystem.kMaxSpeedMetersPerSecond, 0);

            assertEquals(2.180822825435927, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(0.10008672844112536, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(-2.1785249235967656, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);
            for (SwerveModuleState state : drive.getModuleStates())
            {
                assertModuleState(state,  2.9046378135681152, -90);
            }
        }
    }

    @Test
    public void testRotateClockwise()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, 0, 0, -DriveSubsystem.kMaxSpeedMetersPerSecond);

            assertEquals(0, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(-160.00101848197687, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);


            List<SwerveModuleState> states = drive.getModuleStates();
            double expectedSpeed = 1.0927140712738037;
            assertModuleState(states.get(0), expectedSpeed, -45.0);
            assertModuleState(states.get(1), -expectedSpeed, 45.0);
            assertModuleState(states.get(2), expectedSpeed, 45.0);
            assertModuleState(states.get(3), -expectedSpeed, -45.0);

        }
    }

    @Test
    public void testRotateCounterClockwise()
    {
        try (DriveSubsystem drive = new DriveSubsystem())
        {
            runLoops(drive, 0, 0, DriveSubsystem.kMaxSpeedMetersPerSecond);

            assertEquals(0, drive.getPose().getTranslation().getNorm(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getX(), POSITION_TOLERANCE);
            assertEquals(0, drive.getPose().getY(), POSITION_TOLERANCE);
            assertEquals(160.00101848197687, drive.getPose().getRotation().getDegrees(), ANGLE_TOLERANCE);


            List<SwerveModuleState> states = drive.getModuleStates();
            double expectedSpeed = 1.0927140712738037;
            assertModuleState(states.get(0), -expectedSpeed, -45.0);
            assertModuleState(states.get(1), expectedSpeed, 45.0);
            assertModuleState(states.get(2), -expectedSpeed, 45.0);
            assertModuleState(states.get(3), expectedSpeed, -45.0);

        }
    }
}
