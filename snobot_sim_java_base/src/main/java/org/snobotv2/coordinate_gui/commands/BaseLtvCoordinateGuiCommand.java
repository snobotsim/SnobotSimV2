package org.snobotv2.coordinate_gui.commands;

import org.wpilib.command2.Command;
import org.wpilib.command2.Subsystem;
import org.wpilib.math.controller.LTVUnicycleController;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.kinematics.ChassisVelocities;
import org.wpilib.math.kinematics.DifferentialDriveKinematics;
import org.wpilib.math.kinematics.DifferentialDriveWheelVelocities;
import org.wpilib.math.trajectory.DrivetrainSplineSample;
import org.wpilib.math.trajectory.DrivetrainSplineTrajectory;
import org.wpilib.system.Timer;
import org.snobotv2.coordinate_gui.TrajectoryPublisher;

import static org.wpilib.util.ErrorMessages.requireNonNullParam;

/**
 * A trajectory following command for differential drivetrains, built on the {@link LTVUnicycleController}.
 *
 * This replaces the old Ramsete based command, which was removed from WPILib in 2027. Every member is protected so
 * that it is easy to extend, it publishes to the CoordinateGui network tables, and it hands the goal velocities and
 * accelerations to the subclass so that "smart" motor controllers can be used for the velocity control.
 */
@SuppressWarnings({"PMD.FieldNamingConventions", "checkstyle:LeftCurly", "checkstyle:MemberName"}) // To make this as similar as possible to wpilib version, use their styleguide
public abstract class BaseLtvCoordinateGuiCommand extends Command {
    protected final Timer m_timer = new Timer();
    protected final DrivetrainSplineTrajectory m_trajectory;
    protected final LTVUnicycleController m_follower;
    protected final DifferentialDriveKinematics m_kinematics;
    protected DifferentialDriveWheelVelocities m_prevVelocities;
    protected double m_prevTime;

    // Coordinate Gui
    protected final TrajectoryPublisher m_trajectoryPublisher;
    protected double m_goalVelocityLeft;
    protected double m_goalVelocityRight;

    /**
     * Constructs a new command that, when executed, will follow the provided trajectory.
     *
     * @param trajectory The trajectory to follow.
     * @param follower The LTV unicycle follower used to follow the trajectory.
     * @param kinematics The kinematics for the robot drivetrain.
     * @param requirements The subsystems to require.
     */
    public BaseLtvCoordinateGuiCommand(
        DrivetrainSplineTrajectory trajectory,
        LTVUnicycleController follower,
        DifferentialDriveKinematics kinematics,
        Subsystem... requirements) {
        m_trajectory = requireNonNullParam(trajectory, "trajectory", "BaseLtvCoordinateGuiCommand");
        m_follower = requireNonNullParam(follower, "follower", "BaseLtvCoordinateGuiCommand");
        m_kinematics = requireNonNullParam(kinematics, "kinematics", "BaseLtvCoordinateGuiCommand");

        m_trajectoryPublisher = new TrajectoryPublisher();

        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        m_prevTime = -1;
        DrivetrainSplineSample initialState = m_trajectory.start();
        m_prevVelocities =
            m_kinematics.toWheelVelocities(
                new ChassisVelocities(
                    initialState.forwardVelocity(),
                    0,
                    initialState.curvature * initialState.forwardVelocity()));
        m_timer.reset();
        m_timer.start();

        m_trajectoryPublisher.initialize(m_trajectory);
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

        DifferentialDriveWheelVelocities targetWheelVelocities =
            m_kinematics.toWheelVelocities(
                m_follower.calculate(pose, m_trajectory.sampleAt(curTime)));

        m_goalVelocityLeft = targetWheelVelocities.left;
        m_goalVelocityRight = targetWheelVelocities.right;

        double leftAccelerationSetpoint = (m_goalVelocityLeft - m_prevVelocities.left) / dt;
        double rightAccelerationSetpoint = (m_goalVelocityRight - m_prevVelocities.right) / dt;

        setVelocity(m_goalVelocityLeft, m_goalVelocityRight, leftAccelerationSetpoint, rightAccelerationSetpoint);
        m_prevVelocities = targetWheelVelocities;
        m_prevTime = curTime;

        m_trajectoryPublisher.addMeasurement(pose,
            new DifferentialDriveWheelVelocities(m_goalVelocityLeft, m_goalVelocityRight),
            getCurrentWheelVelocities());
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
        return m_timer.hasElapsed(m_trajectory.duration());
    }

    protected abstract Pose2d getPose();

    protected abstract void setVelocity(double leftVelocityMps, double rightVelocityMps, double leftAccelMpss, double rightAccelMpss);

    protected abstract DifferentialDriveWheelVelocities getCurrentWheelVelocities();
}
