package org.snobotv2.examples.base.commands.auton;

import org.wpilib.command2.Command;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public final class DriveStraightTimedCommand extends Command
{
    private final DrivetrainSubsystem mDrivetrain;
    private final double mSpeed;

    public static Command createCommand(DrivetrainSubsystem drivetrain, double speed, double timeout)
    {
        return new DriveStraightTimedCommand(drivetrain, speed).withTimeout(timeout);
    }

    private DriveStraightTimedCommand(DrivetrainSubsystem drivetrain, double speed)
    {
        mDrivetrain = drivetrain;
        mSpeed = speed;
        addRequirements(drivetrain);
    }

    @Override
    public void execute()
    {
        mDrivetrain.arcadeDrive(mSpeed, 0);
    }
}
