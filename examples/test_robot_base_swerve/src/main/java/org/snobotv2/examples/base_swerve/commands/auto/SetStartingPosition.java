package org.snobotv2.examples.base_swerve.commands.auto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base_swerve.subsystems.BaseSwerveDriveSubsystem;

public class SetStartingPosition extends CommandBase
{
    private final BaseSwerveDriveSubsystem mDriveSubsystem;
    private final Pose2d mPose;

    private int mResetCounter;

    public SetStartingPosition(BaseSwerveDriveSubsystem drive, Pose2d pose)
    {
        mDriveSubsystem = drive;
        mPose = pose;
    }

    @Override
    public void initialize()
    {
        mResetCounter = 0;
    }

    @Override
    public void execute()
    {
        ++mResetCounter;
        mDriveSubsystem.resetOdometry(mPose);
    }

    @Override
    public boolean isFinished()
    {
        return mResetCounter >= 20;
    }
}
