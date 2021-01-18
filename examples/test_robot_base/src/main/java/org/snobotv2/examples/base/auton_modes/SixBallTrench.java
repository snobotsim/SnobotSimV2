package org.snobotv2.examples.base.auton_modes;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.snobotv2.examples.base.TrajectoryFactory;
import org.snobotv2.examples.base.commands.auton.DriveTrajectoryCommand;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class SixBallTrench extends SequentialCommandGroup
{
    public SixBallTrench(DrivetrainSubsystem drivetrain, boolean useVoltage)
    {
        Trajectory driveToTrench = TrajectoryFactory.createDriveToTrench(drivetrain.getConstants());
        Trajectory grabTrenchBalls = TrajectoryFactory.createGrabTrenchBalls(drivetrain.getConstants());
        Trajectory controlPanelToSecondShot = TrajectoryFactory.createControlPanelToSecondShot(drivetrain.getConstants());

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
