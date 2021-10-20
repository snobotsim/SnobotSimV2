package org.snobotv2.examples.base.commands.auton;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import org.snobotv2.examples.base.TrajectoryFactory;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public final class DriveTrajectoryCommand
{
    public static Command createWithVoltage(DrivetrainSubsystem drivetrain, Trajectory trajectory)
    {
        return createWithVoltage(drivetrain, trajectory, true);
    }

    public static Command createWithVoltage(DrivetrainSubsystem drivetrain, Trajectory trajectory, boolean resetOnStart)
    {
        DrivetrainSubsystem.DrivetrainConstants drivetrainConstants = drivetrain.getConstants();

        RamseteCommand ramseteCommand =
                new RamseteCommand(
                        trajectory,
                        drivetrain::getPose,
                        new RamseteController(TrajectoryFactory.AutoConstants.kRamseteB, TrajectoryFactory.AutoConstants.kRamseteZeta),
                        new SimpleMotorFeedforward(
                                drivetrainConstants.getKsVolts(),
                                drivetrainConstants.getKvVoltSecondsPerMeter(),
                                drivetrainConstants.getKaVoltSecondsSquaredPerMeter()),
                        drivetrainConstants.getKinematics(),
                        drivetrain::getWheelSpeeds,
                        new PIDController(TrajectoryFactory.DriveConstants.kPDriveVel, 0, 0),
                        new PIDController(TrajectoryFactory.DriveConstants.kPDriveVel, 0, 0),
                        // RamseteCommand passes volts to the callback
                        drivetrain::tankDriveVolts,
                        drivetrain);


        Command runThenStop = ramseteCommand.andThen(() -> drivetrain.stop());

        if (resetOnStart)
        {
            Command resetPose  = new InstantCommand(() -> drivetrain.resetOdometry(trajectory.getInitialPose()));
            return resetPose.andThen(runThenStop);
        }
        return runThenStop;
    }

    public static Command createWithVelocity(DrivetrainSubsystem drivetrain, Trajectory trajectory)
    {
        return createWithVelocity(drivetrain, trajectory, true);
    }

    public static Command createWithVelocity(DrivetrainSubsystem drivetrain, Trajectory trajectory, boolean resetOnStart)
    {
        DrivetrainSubsystem.DrivetrainConstants drivetrainConstants = drivetrain.getConstants();

        RamseteCommand ramseteCommand =
                new RamseteCommand(
                        trajectory,
                        drivetrain::getPose,
                        new RamseteController(TrajectoryFactory.AutoConstants.kRamseteB, TrajectoryFactory.AutoConstants.kRamseteZeta),
                        drivetrainConstants.getKinematics(),
                        drivetrain::smartVelocityControlMetersPerSec,
                        drivetrain);


        Command runThenStop = ramseteCommand.andThen(() -> drivetrain.stop());

        if (resetOnStart)
        {
            Command resetPose  = new InstantCommand(() -> drivetrain.resetOdometry(trajectory.getInitialPose()));
            return resetPose.andThen(runThenStop);
        }
        return runThenStop;
    }

    private DriveTrajectoryCommand()
    {
    }
}
