package org.snobotv2.coordinate_gui.commands;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.snobotv2.coordinate_gui.RamsetePublisher;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

/**
 * This command is a smarter, more extendable version of wpilib's RamseteCommand.
 *
 * There are many posts on ChiefDelphi and Discord about why you shouldn't use wpilib's version. This implementation
 * exposes more information, and is built to run with the CoordinateGui publishing network tables, as well as natively
 * using "smart" motor controls like REV Robotics and CTRE devices.
 *
 * This is essentially a copy of the wpilib command, but every member is protected, and it hooks directly into the
 * CoordinateGui network tables
 */
@SuppressWarnings({"PMD.FieldNamingConventions", "checkstyle:LeftCurly", "checkstyle:MemberName"}) // To make this as similar as possible to wpilib version, use their styleguide
public abstract class BaseRamseteCoordinateGuiCommand extends Command {
    protected final Timer m_timer = new Timer();
    protected final Trajectory m_trajectory;
    protected final RamseteController m_follower;
    protected final DifferentialDriveKinematics m_kinematics;
    protected DifferentialDriveWheelSpeeds m_prevSpeeds;
    protected double m_prevTime;

    // Coordinate Gui
    protected final RamsetePublisher m_ramsetePublisher;
    protected double m_goalVelocityLeft;
    protected double m_goalVelocityRight;

    /**
     * Constructs a new RamseteCommand that, when executed, will follow the provided trajectory.
     * Performs no PID control and calculates no feedforwards; outputs are the raw wheel speeds from
     * the RAMSETE controller, and will need to be converted into a usable form by the user.
     *
     * @param trajectory The trajectory to follow.
     * @param follower The RAMSETE follower used to follow the trajectory.
     * @param kinematics The kinematics for the robot drivetrain.
     * @param requirements The subsystems to require.
     */
    public BaseRamseteCoordinateGuiCommand(
        Trajectory trajectory,
        RamseteController follower,
        DifferentialDriveKinematics kinematics,
        Subsystem... requirements) {
        m_trajectory = requireNonNullParam(trajectory, "trajectory", "BaseRamseteCoordinateGuiCommand");
        m_follower = requireNonNullParam(follower, "follower", "BaseRamseteCoordinateGuiCommand");
        m_kinematics = requireNonNullParam(kinematics, "kinematics", "BaseRamseteCoordinateGuiCommand");

        m_ramsetePublisher = new RamsetePublisher();

        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        m_prevTime = -1;
        Trajectory.State initialState = m_trajectory.sample(0);
        m_prevSpeeds =
            m_kinematics.toWheelSpeeds(
                new ChassisSpeeds(
                    initialState.velocityMetersPerSecond,
                    0,
                    initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond));
        m_timer.reset();
        m_timer.start();

        m_ramsetePublisher.initialize(m_trajectory);
    }

    @Override
    public void execute() {
        double curTime = m_timer.get();

        if (m_prevTime < 0) {
            setVelocity(0.0, 0.0, 0.0, 0.0);
            m_prevTime = curTime;
            return;
        }

        double dt = curTime - m_prevTime;
        Pose2d pose = getPose();

        DifferentialDriveWheelSpeeds targetWheelSpeeds =
            m_kinematics.toWheelSpeeds(
                m_follower.calculate(pose, m_trajectory.sample(curTime)));

        m_goalVelocityLeft = targetWheelSpeeds.leftMetersPerSecond;
        m_goalVelocityRight = targetWheelSpeeds.rightMetersPerSecond;

        double leftAccelerationSetpoint = (m_goalVelocityLeft - m_prevSpeeds.leftMetersPerSecond) / dt;
        double rightAccelerationSetpoint = (m_goalVelocityRight - m_prevSpeeds.rightMetersPerSecond) / dt;

        setVelocity(m_goalVelocityLeft, m_goalVelocityRight, leftAccelerationSetpoint, rightAccelerationSetpoint);
        m_prevSpeeds = targetWheelSpeeds;
        m_prevTime = curTime;

        m_ramsetePublisher.addMeasurement(pose,
            new DifferentialDriveWheelSpeeds(m_goalVelocityLeft, m_goalVelocityRight),
            getCurrentWheelSpeeds());
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();

        if (interrupted) {
            setVelocity(0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(m_trajectory.getTotalTimeSeconds());
    }

    protected abstract Pose2d getPose();

    protected abstract void setVelocity(double leftVelocityMps, double rightVelocityMps, double leftAccelMpss, double rightAccelMpss);

    protected abstract DifferentialDriveWheelSpeeds getCurrentWheelSpeeds();
}
