package org.snobotv2.examples.catchall;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.catchall.subsystems.CatchallSubsystem;

public class RobotContainer
{
    private final CatchallSubsystem mCatchallSubsystem; // NOPMD

    public RobotContainer()
    {
        mCatchallSubsystem = new CatchallSubsystem();

        CameraServer.getInstance().startAutomaticCapture();
    }

    public Command getAutonomousCommand()
    {
        return null;
    }

}
