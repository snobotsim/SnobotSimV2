// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.snobotv2.examples.ctre_swerve.subsystems;

import org.snobotv2.examples.base_swerve.subsystems.BaseSwerveDriveSubsystem;

public class DriveSubsystem extends BaseSwerveDriveSubsystem
{
    public DriveSubsystem()
    {
        super(SwerveModule::new);
    }
}
