package org.snobotv2.examples.base;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.auton_modes.SixBallTrench;
import org.snobotv2.examples.base.commands.auton.DriveStraightTimedCommand;
import org.snobotv2.examples.base.commands.auton.DriveTrajectoryCommand;
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
        new PowerDistributionPanel(); // TODO move
        mAutonChooser = new SendableChooser<>();
        mAutonChooser.addOption("Drive Straight Forward Timed", DriveStraightTimedCommand.createCommand(drivetrain, 1, 4));
        mAutonChooser.setDefaultOption("Turn To Angle +90", new TurnToAngle(drivetrain, 90));
        mAutonChooser.addOption("Turn To Angle -90", new TurnToAngle(drivetrain, -90));
        mAutonChooser.addOption("Ramsete (Voltage) Zig Zag", DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createZigZagTrajectoryCommand(drivetrain.getConstants())));
        mAutonChooser.addOption("Ramsete (Voltage) S-Curve", DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createSCurve(drivetrain.getConstants())));
        mAutonChooser.addOption("Ramsete (Voltage) 6-ball", new SixBallTrench(drivetrain, true));
        mAutonChooser.addOption("Ramsete (Velocity) Zig Zag", DriveTrajectoryCommand.createWithVelocity(drivetrain, TrajectoryFactory.createZigZagTrajectoryCommand(drivetrain.getConstants())));
        mAutonChooser.addOption("Ramsete (Velocity) S-Curve", DriveTrajectoryCommand.createWithVelocity(drivetrain, TrajectoryFactory.createSCurve(drivetrain.getConstants())));
        mAutonChooser.addOption("Ramsete (Velocity) 6-ball", new SixBallTrench(drivetrain, false));

        SmartDashboard.putData(mAutonChooser);
    }

    public Command getAutonCommand()
    {
        return mAutonChooser.getSelected();
    }
}
