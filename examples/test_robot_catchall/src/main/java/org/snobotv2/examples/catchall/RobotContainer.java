package org.snobotv2.examples.catchall;

import org.wpilib.vision.stream.CameraServer;
import org.wpilib.command2.Command;
import org.snobotv2.examples.catchall.subsystems.CatchallSubsystem;

public class RobotContainer
{
    private final CatchallSubsystem mCatchallSubsystem; // NOPMD

    public RobotContainer()
    {
        mCatchallSubsystem = new CatchallSubsystem();

        CameraServer.startAutomaticCapture();
    }

    public Command getAutonomousCommand()
    {
        return null;
    }

}
