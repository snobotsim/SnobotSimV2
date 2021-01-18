package org.snobotv2.examples.base;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.snobotv2.examples.base.commands.DriveWithHaloJoysticksCommand;
import org.snobotv2.examples.base.commands.ElevatorGoToPositionCommand;
import org.snobotv2.examples.base.commands.ElevatorManuallyMoveCommand;
import org.snobotv2.examples.base.commands.MovePunchCommand;
import org.snobotv2.examples.base.commands.RunShooterAtRpmCommand;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.examples.base.subsystems.PunchSubsystem;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;

public class ButtonMapping
{
    @SuppressWarnings("PMD.UnusedFormalParameter")
    public ButtonMapping(DrivetrainSubsystem drivetrain, ElevatorSubsystem elevator, PunchSubsystem punch, ShooterSubsystem shooter)
    {
        XboxController driverJoystick = new XboxController(0);
        XboxController operatorJoystick = new XboxController(1);

        DriveWithHaloJoysticksCommand defaultDriveCommand = new DriveWithHaloJoysticksCommand(drivetrain, driverJoystick);
        drivetrain.setDefaultCommand(defaultDriveCommand);

        ElevatorManuallyMoveCommand defaultElevatorCommand = new ElevatorManuallyMoveCommand(elevator, operatorJoystick);
        elevator.setDefaultCommand(defaultElevatorCommand);

        new JoystickButton(operatorJoystick, 1).whileHeld(new ElevatorGoToPositionCommand(elevator, 30));
        new JoystickButton(operatorJoystick, 2).whileHeld(new ElevatorGoToPositionCommand(elevator, 45));
        new JoystickButton(operatorJoystick, 3).whileHeld(new ElevatorGoToPositionCommand(elevator, 60));

        new JoystickButton(operatorJoystick, 4).whenPressed(new MovePunchCommand(punch, true));
        new JoystickButton(operatorJoystick, 5).whenPressed(new MovePunchCommand(punch, false));

        new JoystickButton(operatorJoystick, 6).whileHeld(new RunShooterAtRpmCommand(shooter, 2500));
        new JoystickButton(operatorJoystick, 7).whileHeld(new RunShooterAtRpmCommand(shooter, 3500));
    }
}
