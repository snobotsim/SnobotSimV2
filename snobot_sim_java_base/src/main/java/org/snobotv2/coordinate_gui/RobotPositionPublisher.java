package org.snobotv2.coordinate_gui;

import org.wpilib.networktables.NetworkTable;
import org.wpilib.networktables.NetworkTableEntry;
import org.wpilib.networktables.NetworkTableInstance;
import org.wpilib.math.geometry.Pose2d;

public class RobotPositionPublisher
{
    private final double[] mData;
    private final NetworkTableEntry mEntry;
    private final int mPublishingFrequency;
    private int mCounter;

    public RobotPositionPublisher()
    {
        this(NetworkTableInstance.getDefault().getTable("CoordinateGui"), 5); // Publish every 5 loops, or 100ms
    }

    public RobotPositionPublisher(NetworkTable coordinateGuiTable, int publishingFrequency)
    {
        coordinateGuiTable.getEntry(".type").setString("CoordinateGui");

        mPublishingFrequency = publishingFrequency;
        mEntry = coordinateGuiTable.getSubTable("RobotPosition").getEntry("position");
        mData = new double[3];
    }

    public void publish(Pose2d pose)
    {
        ++mCounter;
        if (mCounter % mPublishingFrequency == 0)
        {
            mData[0] = pose.getX();
            mData[1] = pose.getY();
            mData[2] = pose.getRotation().getDegrees();
            mEntry.setDoubleArray(mData);
        }
    }
}
