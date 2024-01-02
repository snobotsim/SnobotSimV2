package org.snobotv2.coordinate_gui;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

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

        StringBuilder output = new StringBuilder();

        for (Trajectory.State state : trajectory.getStates())
        {
            output
                    .append(state.timeSeconds).append(',')
                    .append(state.velocityMetersPerSecond).append(',')
                    .append(state.poseMeters.getTranslation().getX()).append(',')
                    .append(state.poseMeters.getTranslation().getY()).append(',')
                    .append(state.poseMeters.getRotation().getDegrees()).append(',');
        }

        mIdealTableEntry.setString(output.toString());
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
