package org.snobotv2.examples.base.commands.auton;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;

public class TurnToAngle extends Command
{
    private final PIDController mController;
    private final DrivetrainSubsystem mDrivetrain;
    private final double mGoal;

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public TurnToAngle(DrivetrainSubsystem drivetrain, double goal)
    {
        mController = new PIDController(.02, 0, 0.001);
        mController.enableContinuousInput(-180, 180);
        mController.setTolerance(3, 1);

        mDrivetrain = drivetrain;
        mGoal = goal;

        addRequirements(mDrivetrain);
    }

    @Override
    public void execute()
    {
        double currentAngle = mDrivetrain.getHeadingDegrees();
        double output = mController.calculate(currentAngle, mGoal);
        mDrivetrain.arcadeDrive(0, output);
    }

    @Override
    public boolean isFinished()
    {
        return mController.atSetpoint();
    }
}
