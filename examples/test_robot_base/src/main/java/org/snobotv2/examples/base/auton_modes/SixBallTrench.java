package org.snobotv2.examples.base.auton_modes;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.snobotv2.examples.base.TrajectoryFactory;
import org.snobotv2.examples.base.commands.auton.DriveTrajectoryCommand;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class SixBallTrench extends SequentialCommandGroup
{
    public SixBallTrench(DrivetrainSubsystem drivetrain)
    {
        addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createDriveToTrench(drivetrain.getConstants())));
        addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createGrabTrenchBalls(drivetrain.getConstants()), false));
        addCommands(DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createControlPanelToSecondShot(drivetrain.getConstants()), false));
    }
}
