package org.snobotv2.examples.base.commands;

import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;

public class ElevatorGoToPositionCommand extends Command
{
    private final double mHeight;
    private final ElevatorSubsystem mElevator;

    public ElevatorGoToPositionCommand(ElevatorSubsystem elevator, double aHeight)
    {
        mHeight = aHeight;
        mElevator = elevator;
        addRequirements(elevator);
    }

    @Override
    public void execute()
    {
        mElevator.goToPosition(mHeight);
    }

    @Override
    public boolean isFinished()
    {
        return mElevator.isAtHeight(mHeight);
    }

    @Override
    public void end(boolean interrupted)
    {
        mElevator.stop();
    }
}
