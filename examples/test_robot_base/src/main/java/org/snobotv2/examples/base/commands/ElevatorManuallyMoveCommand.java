package org.snobotv2.examples.base.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;

public class ElevatorManuallyMoveCommand extends Command
{
    private final XboxController mJoystick;
    private final ElevatorSubsystem mElevator;

    public ElevatorManuallyMoveCommand(ElevatorSubsystem elevator, XboxController joystick)
    {
        mJoystick = joystick;
        mElevator = elevator;
        addRequirements(elevator);
    }

    @Override
    public void execute()
    {
        mElevator.moveManually(-mJoystick.getLeftY());
    }
}
