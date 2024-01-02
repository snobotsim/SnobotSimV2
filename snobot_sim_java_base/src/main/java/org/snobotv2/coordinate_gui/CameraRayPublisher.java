package org.snobotv2.coordinate_gui;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.geometry.Pose2d;

public class CameraRayPublisher
{
    private final NetworkTableEntry mPositions;

    public CameraRayPublisher()
    {
        this(NetworkTableInstance.getDefault().getTable("CoordinateGui"));
    }

    public CameraRayPublisher(NetworkTable coordinateGuiTable)
    {
        coordinateGuiTable.getEntry(".type").setString("CoordinateGui");

        NetworkTable limelightRayTable = coordinateGuiTable.getSubTable("CameraSim");
        limelightRayTable.getEntry(".type").setString("CameraSim");
        mPositions =  limelightRayTable.getEntry("Positions");
    }

    public void clear()
    {
        mPositions.setDoubleArray(new double[]{});
    }

    public void publish(Pose2d robotPose, Pose2d... targets)
    {
        double[] data = new double[targets.length * 4];

        for (int i = 0; i < targets.length; ++i)
        {
            data[i * 4 + 0] = robotPose.getX();
            data[i * 4 + 1] = robotPose.getY();
            data[i * 4 + 2] = targets[i].getX();
            data[i * 4 + 3] = targets[i].getY();
        }

        mPositions.setDoubleArray(data);
    }
}
