package org.snobotv2.examples.base.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;

public class RunShooterAtRpmCommand extends CommandBase
{
    private final double mRpm;
    private final ShooterSubsystem mShooter;

    public RunShooterAtRpmCommand(ShooterSubsystem shooter, double rpm)
    {
        mRpm = rpm;
        mShooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute()
    {
        mShooter.spinAtRpm(mRpm);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    @Override
    public void end(boolean interrupted)
    {
        mShooter.stop();
    }
}
