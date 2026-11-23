package org.snobotv2.examples.base.auton_modes;

import org.wpilib.command2.Command;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.power.PowerDistribution;
import org.wpilib.tunable.Selectable;
import org.wpilib.tunable.Tunables;
import org.snobotv2.examples.base.trajectories.TrajectoryFactory;
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
    private final Selectable<Command> mAutonChooser;

    @SuppressWarnings("PMD.UnusedFormalParameter")
    public AutonomousFactory(DrivetrainSubsystem drivetrain, ElevatorSubsystem elevator, PunchSubsystem punch, ShooterSubsystem shooter, LedSubsystem leds)
    {
        new PowerDistribution(CANPort.CAN_S0); // TODO move
        mAutonChooser = new Selectable<>();
        mAutonChooser.add("Drive Straight Forward Timed", DriveStraightTimedCommand.createCommand(drivetrain, 1, 4));
        mAutonChooser.addDefault("Turn To Angle +90", new TurnToAngle(drivetrain, 90));
        mAutonChooser.add("Turn To Angle -90", new TurnToAngle(drivetrain, -90));
        mAutonChooser.add("LTV (Voltage) Zig Zag", DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createZigZagTrajectoryCommand(drivetrain.getConstants())));
        mAutonChooser.add("LTV (Voltage) S-Curve", DriveTrajectoryCommand.createWithVoltage(drivetrain, TrajectoryFactory.createSCurve(drivetrain.getConstants())));
        mAutonChooser.add("LTV (Voltage) 6-ball", new SixBallTrench(drivetrain, true));
        mAutonChooser.add("LTV (Velocity) Zig Zag", DriveTrajectoryCommand.createWithVelocity(drivetrain, TrajectoryFactory.createZigZagTrajectoryCommand(drivetrain.getConstants())));
        mAutonChooser.add("LTV (Velocity) S-Curve", DriveTrajectoryCommand.createWithVelocity(drivetrain, TrajectoryFactory.createSCurve(drivetrain.getConstants())));
        mAutonChooser.add("LTV (Velocity) 6-ball", new SixBallTrench(drivetrain, false));

        Tunables.publish("Autonomous", mAutonChooser);
    }

    public Command getAutonCommand()
    {
        return mAutonChooser.getSelected();
    }
}
