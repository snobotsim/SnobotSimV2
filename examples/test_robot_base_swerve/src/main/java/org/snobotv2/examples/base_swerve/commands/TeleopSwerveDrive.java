// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.snobotv2.examples.base_swerve.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base_swerve.subsystems.BaseSwerveDriveSubsystem;

public class TeleopSwerveDrive extends Command
{
    private final BaseSwerveDriveSubsystem mDrive;
    private final XboxController mController;

    public TeleopSwerveDrive(BaseSwerveDriveSubsystem drive, XboxController controller)
    {
        mDrive = drive;
        mController = controller;
        addRequirements(drive);
    }

    @Override
    public void execute()
    {
        double xSpeed = mController.getLeftX() * BaseSwerveDriveSubsystem.kMaxSpeedMetersPerSecond;
        double ySpeed = -mController.getLeftY() * BaseSwerveDriveSubsystem.kMaxSpeedMetersPerSecond;
        double rot = mController.getRightX() * BaseSwerveDriveSubsystem.kMaxRotationRadiansPerSecond;
        mDrive.teleDrive(xSpeed, ySpeed, rot);
    }

    @Override
    public void end(boolean interrupted)
    {
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
