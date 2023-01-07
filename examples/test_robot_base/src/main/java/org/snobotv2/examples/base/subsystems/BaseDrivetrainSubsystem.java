package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class BaseDrivetrainSubsystem extends SubsystemBase implements DrivetrainSubsystem
{
    protected final DifferentialDriveOdometry mOdometry;
    protected final Field2d mField;

    public BaseDrivetrainSubsystem()
    {
        mOdometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);
        mField = new Field2d();
        SmartDashboard.putData(mField);
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
        mField.setRobotPose(mOdometry.getPoseMeters());
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
        return mOdometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    @Override
    public DifferentialDriveWheelSpeeds getWheelSpeeds()
    {
        return new DifferentialDriveWheelSpeeds(getLeftRate(), getRightRate());
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
