package org.snobotv2.examples.base_swerve.subsystems;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import org.snobotv2.sim_wrappers.SwerveModuleSimWrapper;

public interface BaseSwerveModule extends Sendable
{
    // MK4i - L1
    double TURNING_GEAR_RATION = (50.0 / 14.0) * (60.0 / 10.0);
    double DRIVE_GEAR_RATION = (50.0 / 14.0) * (19.0 / 25.0) * (45.0 / 15.0);

    String getName();

    SwerveModuleState getState();

    SwerveModuleState getDesiredState();

    double getDriveMotorPercentage();

    double getTurningMotorPercentage();

    void setOpenLoop(double turnSpeed, double driveSpeed);

    void periodic();

    void setDesiredState(SwerveModuleState desiredState, boolean b);

    void resetEncoders();

    SwerveModuleSimWrapper getSimWrapper();

    void stop();
}
