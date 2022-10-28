package org.snobotv2.examples.base_swerve.commands.tuning;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.snobotv2.examples.base_swerve.subsystems.BaseSwerveDriveSubsystem;


public class ManualMoveSwerveModuleCommand extends CommandBase
{
    private final BaseSwerveDriveSubsystem mDriveSubsystem;
    private final BaseSwerveDriveSubsystem.SwerveModulePosition mModulePosition;
    private static final double TURNING_SPEED = 1;
    private static final double DRIVING_SPEED = 0;

    public ManualMoveSwerveModuleCommand(BaseSwerveDriveSubsystem driveSubsystem, BaseSwerveDriveSubsystem.SwerveModulePosition modulePosition)
    {
        mDriveSubsystem = driveSubsystem;
        mModulePosition = modulePosition;
        addRequirements(this.mDriveSubsystem);
    }

    @Override
    public void initialize()
    {
    }

    @Override
    public void execute()
    {
        mDriveSubsystem.manuallyMoveModule(mModulePosition, TURNING_SPEED, DRIVING_SPEED);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    @Override
    public void end(boolean interrupted)
    {
        mDriveSubsystem.stop();
    }
}
