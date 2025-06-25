package org.snobotv2.examples.base.auton_modes;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.commands.auton.DriveStraightTimedCommand;
import org.snobotv2.examples.base.commands.auton.TurnToAngle;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.examples.base.subsystems.LedSubsystem;
import org.snobotv2.examples.base.subsystems.PunchSubsystem;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;

public class AutonomousFactory
{
    private final SendableChooser<Command> mAutonChooser;

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public AutonomousFactory(DrivetrainSubsystem drivetrain, ElevatorSubsystem elevator, PunchSubsystem punch, ShooterSubsystem shooter, LedSubsystem leds)
    {
        new PowerDistribution(); // TODO move
        mAutonChooser = new SendableChooser<>();
        mAutonChooser.addOption("Drive Straight Forward Timed", DriveStraightTimedCommand.createCommand(drivetrain, 1, 4));
        mAutonChooser.setDefaultOption("Turn To Angle +90", new TurnToAngle(drivetrain, 90));
        mAutonChooser.addOption("Turn To Angle -90", new TurnToAngle(drivetrain, -90));

        SmartDashboard.putData(mAutonChooser);
    }

    public Command getAutonCommand()
    {
        return mAutonChooser.getSelected();
    }
}
