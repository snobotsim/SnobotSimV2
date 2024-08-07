package org.snobotv2.examples.base.trajectories;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

import java.util.List;

public final class TrajectoryFactory
{
    @SuppressWarnings("PMD.DataClass")
    public static final class AutoConstants
    {
        public static final double kMaxSpeedMetersPerSecond = 3;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;

        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

    public static final class DriveConstants
    {
        public static final double kPDriveVel = 8.5;


    }

    private TrajectoryFactory()
    {
    }

    public static Trajectory createZigZagTrajectoryCommand(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        TrajectoryConfig config = getTrajectoryConfig(drivetrainConstants);

        return TrajectoryGenerator.generateTrajectory(
                        new Pose2d(0, 0, new Rotation2d(0)),
                        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
                        new Pose2d(3, 0, new Rotation2d(0)),
                        config);
    }

    public static Trajectory createSCurve(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        TrajectoryConfig config = getTrajectoryConfig(drivetrainConstants);

        return TrajectoryGenerator.generateTrajectory(
                new Pose2d(2, 2, new Rotation2d()),
                List.of(),
                new Pose2d(6, 4, new Rotation2d()),
                config);
    }

    public static Trajectory createDriveToTrench(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        TrajectoryConfig config = getTrajectoryConfig(drivetrainConstants);

        return TrajectoryGenerator.generateTrajectory(
                new Pose2d(Units.inchesToMeters(122), Units.inchesToMeters(162 + 31 + 22), new Rotation2d()),
                List.of(),
                new Pose2d(Units.inchesToMeters(207), Units.inchesToMeters(162 + 98 + 22), new Rotation2d()),
                config);
    }

    public static Trajectory createGrabTrenchBalls(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        TrajectoryConfig config = getTrajectoryConfig(drivetrainConstants);

        return TrajectoryGenerator.generateTrajectory(
                new Pose2d(Units.inchesToMeters(207), Units.inchesToMeters(162 + 98 + 22), new Rotation2d()),
                List.of(),
                new Pose2d(Units.inchesToMeters(327), Units.inchesToMeters(162 + 98 + 22), new Rotation2d()),
                config);
    }

    public static Trajectory createControlPanelToSecondShot(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        TrajectoryConfig config = getTrajectoryConfig(drivetrainConstants);
        config.setReversed(true);

        return TrajectoryGenerator.generateTrajectory(
                new Pose2d(Units.inchesToMeters(207), Units.inchesToMeters(162 + 98 + 22), new Rotation2d()),
                List.of(),
                new Pose2d(Units.inchesToMeters(122), Units.inchesToMeters(162 + 31 + 22), new Rotation2d()),
                config);
    }

    public static TrajectoryConfig getTrajectoryConfig(DrivetrainSubsystem.DrivetrainConstants drivetrainConstants)
    {
        // Create a voltage constraint to ensure we don't accelerate too fast
        DifferentialDriveVoltageConstraint autoVoltageConstraint =
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(
                                drivetrainConstants.getKsVolts(),
                                drivetrainConstants.getKvVoltSecondsPerMeter(),
                                drivetrainConstants.getKaVoltSecondsSquaredPerMeter()),
                        drivetrainConstants.getKinematics(),
                        10);

        // Create config for trajectory
        return new TrajectoryConfig(
                        AutoConstants.kMaxSpeedMetersPerSecond,
                        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(drivetrainConstants.getKinematics())
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint);
    }

}
