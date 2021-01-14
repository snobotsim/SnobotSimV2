package org.snobotv2.examples.base.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base.subsystems.PunchSubsystem;

public class MovePunchCommand extends CommandBase
{
    private final boolean mExtend;
    private final PunchSubsystem mPunch;

    public MovePunchCommand(PunchSubsystem punch, boolean extend)
    {
        mExtend = extend;
        mPunch = punch;
        addRequirements(punch);
    }

    @Override
    public void execute()
    {
        if (mExtend)
        {
            mPunch.extend();
        } else
        {
            mPunch.retract();
        }
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}
