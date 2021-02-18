package org.snobotv2.coordinate_gui;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class RamsetePublisher
{
    private final double[] mMeasurementData;
    private final NetworkTableEntry mIdealTableEntry;
    private final NetworkTableEntry mMeasuredTableEntry;
    private final Timer mTimer;

    public RamsetePublisher()
    {
        this(NetworkTableInstance.getDefault().getTable("CoordinateGui"));
    }

    public RamsetePublisher(NetworkTable coordinateGuiTable)
    {
        coordinateGuiTable.getEntry(".type").setString("CoordinateGui");

        NetworkTable trajectoryTable = coordinateGuiTable.getSubTable("Ramsete Namespace");
        trajectoryTable.getEntry(".type").setString("Ramsete Namespace");

        mMeasuredTableEntry = trajectoryTable.getEntry("Measured");
        mIdealTableEntry = trajectoryTable.getEntry("Ideal");

        mTimer = new Timer();

        mMeasurementData = new double[8];
    }

    public void initialize(Trajectory trajectory)
    {
        mTimer.reset();
        mTimer.start();

        double[] data = new double[trajectory.getStates().size() * 5];

        int index = 0;
        for (Trajectory.State state : trajectory.getStates()) 
        {
            data[index++] = state.timeSeconds;
            data[index++] = state.velocityMetersPerSecond;
            data[index++] = state.poseMeters.getTranslation().getX();
            data[index++] = state.poseMeters.getTranslation().getY();
            data[index++] = state.poseMeters.getRotation().getDegrees();
        }

        mIdealTableEntry.setDoubleArray(data);
    }

    public void addMeasurement(Pose2d pose, DifferentialDriveWheelSpeeds goalVelocity, DifferentialDriveWheelSpeeds measuredVelocity)
    {
        mMeasurementData[0] = mTimer.get();
        mMeasurementData[1] = pose.getX();
        mMeasurementData[2] = pose.getY();
        mMeasurementData[3] = pose.getRotation().getDegrees();
        mMeasurementData[4] = goalVelocity.leftMetersPerSecond;
        mMeasurementData[5] = goalVelocity.rightMetersPerSecond;
        mMeasurementData[6] = measuredVelocity.leftMetersPerSecond;
        mMeasurementData[7] = measuredVelocity.rightMetersPerSecond;

        mMeasuredTableEntry.setDoubleArray(mMeasurementData);
    }
}
