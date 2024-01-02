package org.snobotv2.examples.phoenix5_swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import org.snobotv2.examples.base_swerve.commands.TeleopSwerveDrive;
import org.snobotv2.examples.base_swerve.commands.auto.SetStartingPosition;
import org.snobotv2.examples.base_swerve.commands.tuning.ManualMoveSwerveModuleCommand;
import org.snobotv2.examples.phoenix5_swerve.subsystems.DriveSubsystem;

public class RobotContainer
{
    private final DriveSubsystem mDrivetrain;

    private final XboxController mDriverJoystick = new XboxController(0);


    public RobotContainer()
    {
        mDrivetrain = new DriveSubsystem(true);
        configureButtonBindings();
        addTestCommands();
    }

    private void addTestCommands()
    {
        ShuffleboardTab debugTab = Shuffleboard.getTab("Test Commands");
        debugTab.add("Move FL", new ManualMoveSwerveModuleCommand(mDrivetrain, DriveSubsystem.SwerveModulePositionTypes.FRONT_LEFT));
        debugTab.add("Move FR", new ManualMoveSwerveModuleCommand(mDrivetrain, DriveSubsystem.SwerveModulePositionTypes.FRONT_RIGHT));
        debugTab.add("Move RL", new ManualMoveSwerveModuleCommand(mDrivetrain, DriveSubsystem.SwerveModulePositionTypes.REAR_LEFT));
        debugTab.add("Move RR", new ManualMoveSwerveModuleCommand(mDrivetrain, DriveSubsystem.SwerveModulePositionTypes.REAR_RIGHT));

        debugTab.add("Reset Position", new SetStartingPosition(mDrivetrain, new Pose2d(0, 0, Rotation2d.fromDegrees(0))));
    }

    private void configureButtonBindings()
    {
        mDrivetrain.setDefaultCommand(new TeleopSwerveDrive(mDrivetrain, mDriverJoystick));
    }

    public Command getAutonomousCommand()
    {
        return null;
    }

}
