package org.snobotv2.examples.base.subsystems;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.DifferentialDriveOdometry;
import org.wpilib.math.kinematics.DifferentialDriveWheelVelocities;
import org.wpilib.smartdashboard.Field2d;
import org.wpilib.command2.SubsystemBase;
import org.wpilib.telemetry.Telemetry;

public abstract class BaseDrivetrainSubsystem extends SubsystemBase implements DrivetrainSubsystem
{
    protected final DifferentialDriveOdometry mOdometry;
    protected final Field2d mField;

    public BaseDrivetrainSubsystem()
    {
        mOdometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);
        mField = new Field2d();
    }

    public double getPositionError(double leftGoal, double rightGoal)
    {
        double dl = leftGoal - getLeftDistance();
        double dr = rightGoal - getRightDistance();

        return Math.sqrt(dl * dl + dr * dr);
    }

    protected void updateOdometry()
    {
        // Update the odometry in the periodic block
        mOdometry.update(
                getRotation2d(),
                getLeftDistance(),
               getRightDistance());
        mField.setRobotPose(mOdometry.getPose());
        Telemetry.log("Field", mField);
    }

    public Rotation2d getRotation2d()
    {
        return Rotation2d.fromDegrees(-getHeadingDegrees());
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    @Override
    public Pose2d getPose()
    {
        return mOdometry.getPose();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    @Override
    public DifferentialDriveWheelVelocities getWheelSpeeds()
    {
        return new DifferentialDriveWheelVelocities(getLeftRate(), getRightRate());
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    @Override
    public void resetOdometry(Pose2d pose)
    {
        mOdometry.resetPosition(getRotation2d(), getLeftDistance(), getRightDistance(), pose);
        resetSimOdometry(pose);
    }

    protected abstract void resetSimOdometry(Pose2d pose);

    @Override
    public void stop()
    {
        arcadeDrive(0, 0);
    }

}
