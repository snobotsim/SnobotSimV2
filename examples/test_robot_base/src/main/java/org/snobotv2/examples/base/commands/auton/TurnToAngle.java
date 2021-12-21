package org.snobotv2.examples.base.commands.auton;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class TurnToAngle extends PIDCommand
{
    public TurnToAngle(DrivetrainSubsystem drivetrain, double goal)
    {
        super(new PIDController(.02, 0, 0.001),
                drivetrain::getHeadingDegrees,
                goal,
                (double output) -> drivetrain.arcadeDrive(0, output),
                drivetrain);

        getController().enableContinuousInput(-180, 180);
        getController().setTolerance(3, 1);
    }

    @Override
    public boolean isFinished()
    {
        return m_controller.atSetpoint();
    }
}
