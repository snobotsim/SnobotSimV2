package org.snobotv2.examples.base.commands;

import org.wpilib.driverstation.XboxController;
import org.wpilib.command2.Command;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class DriveWithHaloJoysticksCommand extends Command
{
    private final DrivetrainSubsystem mDrivetrain;
    private final XboxController mJoystick;

    public DriveWithHaloJoysticksCommand(DrivetrainSubsystem drivetrain, XboxController joystick)
    {
        mDrivetrain = drivetrain;
        mJoystick = joystick;
        addRequirements(drivetrain);
    }

    @Override
    public void execute()
    {
        mDrivetrain.arcadeDrive(-mJoystick.getLeftY(), mJoystick.getRightX());
    }
}
