package org.snobotv2.examples.base.auton_modes;

import org.wpilib.math.trajectory.DrivetrainSplineTrajectory;
import org.wpilib.command2.SequentialCommandGroup;
import org.snobotv2.examples.base.trajectories.TrajectoryFactory;
import org.snobotv2.examples.base.commands.auton.DriveTrajectoryCommand;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class SixBallTrench extends SequentialCommandGroup
{
    public SixBallTrench(DrivetrainSubsystem drivetrain, boolean useVoltage)
    {
        DrivetrainSplineTrajectory driveToTrench = TrajectoryFactory.createDriveToTrench(drivetrain.getConstants());
        DrivetrainSplineTrajectory grabTrenchBalls = TrajectoryFactory.createGrabTrenchBalls(drivetrain.getConstants());
        DrivetrainSplineTrajectory controlPanelToSecondShot = TrajectoryFactory.createControlPanelToSecondShot(drivetrain.getConstants());

        if (useVoltage)
        {
            addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, driveToTrench));
            addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, grabTrenchBalls));
            addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, controlPanelToSecondShot));
        }
        else
        {
            addCommands(DriveTrajectoryCommand.createWithVelocity(drivetrain, driveToTrench));
            addCommands(DriveTrajectoryCommand.createWithVelocity(drivetrain, grabTrenchBalls, false));
            addCommands(DriveTrajectoryCommand.createWithVelocity(drivetrain, controlPanelToSecondShot, false));
        }
    }
}
