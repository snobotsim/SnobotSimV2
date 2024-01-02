package org.snobotv2.examples.phoenix5;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base.auton_modes.AutonomousFactory;
import org.snobotv2.examples.base.ButtonMapping;
import org.snobotv2.examples.phoenix5.subsystems.CtreDrivetrain;
import org.snobotv2.examples.phoenix5.subsystems.CtreElevatorSubsystem;
import org.snobotv2.examples.phoenix5.subsystems.CtreShooterSubsystem;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.examples.base.subsystems.LedSubsystem;
import org.snobotv2.examples.base.subsystems.PunchSubsystem;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;

public class RobotContainer
{
    private final DrivetrainSubsystem mDrivetrain;
    private final ElevatorSubsystem mElevatorSubsystem;
    private final ShooterSubsystem mShooter;

    private final PunchSubsystem mPunch;
    private final LedSubsystem mLeds; // NOPMD

    private final AutonomousFactory mAutonFactory;

    public RobotContainer()
    {
        mDrivetrain = new CtreDrivetrain();
        mElevatorSubsystem = new CtreElevatorSubsystem();
        mShooter = new CtreShooterSubsystem();

        mPunch = new PunchSubsystem();
        mLeds = new LedSubsystem();

        mAutonFactory = new AutonomousFactory(mDrivetrain, mElevatorSubsystem, mPunch, mShooter, mLeds);

        configureButtonBindings();
        CameraServer.startAutomaticCapture();
    }

    private void configureButtonBindings()
    {
        new ButtonMapping(mDrivetrain, mElevatorSubsystem, mPunch, mShooter);
    }

    public Command getAutonomousCommand()
    {
        return mAutonFactory.getAutonCommand();
    }

}
