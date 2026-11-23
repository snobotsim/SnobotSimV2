package org.snobotv2.coordinate_gui;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.kinematics.DifferentialDriveWheelVelocities;
import org.wpilib.math.trajectory.DrivetrainSplineSample;
import org.wpilib.math.trajectory.DrivetrainSplineTrajectory;
import org.wpilib.networktables.NetworkTable;
import org.wpilib.networktables.NetworkTableEntry;
import org.wpilib.networktables.NetworkTableInstance;
import org.wpilib.system.Timer;

/**
 * Publishes the ideal and measured trajectory to the CoordinateGui.
 *
 * The network table layout is unchanged from the old Ramsete publisher, so the CoordinateGui can keep reading the
 * "Ramsete Namespace" table.
 */
public class TrajectoryPublisher
{
    private final double[] mMeasurementData;
    private final NetworkTableEntry mIdealTableEntry;
    private final NetworkTableEntry mMeasuredTableEntry;
    private final Timer mTimer;

    public TrajectoryPublisher()
    {
        this(NetworkTableInstance.getDefault().getTable("CoordinateGui"));
    }

    public TrajectoryPublisher(NetworkTable coordinateGuiTable)
    {
        coordinateGuiTable.getEntry(".type").setString("CoordinateGui");

        NetworkTable trajectoryTable = coordinateGuiTable.getSubTable("Ramsete Namespace");
        trajectoryTable.getEntry(".type").setString("Ramsete Namespace");

        mMeasuredTableEntry = trajectoryTable.getEntry("Measured");
        mIdealTableEntry = trajectoryTable.getEntry("Ideal");

        mTimer = new Timer();

        mMeasurementData = new double[8];
    }

    public void initialize(DrivetrainSplineTrajectory trajectory)
    {
        mTimer.reset();
        mTimer.start();

        StringBuilder output = new StringBuilder();

        for (DrivetrainSplineSample sample : trajectory.getSamples())
        {
            output
                    .append(sample.time).append(',')
                    .append(sample.forwardVelocity()).append(',')
                    .append(sample.pose.getTranslation().getX()).append(',')
                    .append(sample.pose.getTranslation().getY()).append(',')
                    .append(sample.pose.getRotation().getDegrees()).append(',');
        }

        mIdealTableEntry.setString(output.toString());
    }

    public void addMeasurement(Pose2d pose, DifferentialDriveWheelVelocities goalVelocity, DifferentialDriveWheelVelocities measuredVelocity)
    {
        mMeasurementData[0] = mTimer.get();
        mMeasurementData[1] = pose.getX();
        mMeasurementData[2] = pose.getY();
        mMeasurementData[3] = pose.getRotation().getDegrees();
        mMeasurementData[4] = goalVelocity.left;
        mMeasurementData[5] = goalVelocity.right;
        mMeasurementData[6] = measuredVelocity.left;
        mMeasurementData[7] = measuredVelocity.right;

        mMeasuredTableEntry.setDoubleArray(mMeasurementData);
    }
}
