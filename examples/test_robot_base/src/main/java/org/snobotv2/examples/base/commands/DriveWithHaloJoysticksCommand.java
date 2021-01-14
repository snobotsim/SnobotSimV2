package org.snobotv2.examples.base.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class DriveWithHaloJoysticksCommand extends CommandBase
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
        mDrivetrain.arcadeDrive(-mJoystick.getY(GenericHID.Hand.kLeft), mJoystick.getX(GenericHID.Hand.kRight));
    }
}
