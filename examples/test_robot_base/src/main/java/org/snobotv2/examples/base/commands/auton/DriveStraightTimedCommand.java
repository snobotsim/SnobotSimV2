package org.snobotv2.examples.base.commands.auton;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public final class DriveStraightTimedCommand extends CommandBase
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
