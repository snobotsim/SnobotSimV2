package org.snobotv2.camera;

import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"PMD.ShortVariable"})
public class LimelightSimulator extends BaseCameraSimulator
{
    private static final double HORIZONTAL_FOV = 59.6 / 2;

    private final double mCameraToTargetHeight;
    private final NetworkTableEntry mTV;
    private final NetworkTableEntry mTX;
    private final NetworkTableEntry mTY;
    private final NetworkTableEntry mTA;
    private double mTargetArea;

    public LimelightSimulator(List<TargetLocation> targets, double cameraToTargetHeight)
    {
        this(targets, new Transform2d(), cameraToTargetHeight, Double.MAX_VALUE, NetworkTableInstance.getDefault().getTable("limelight"));
    }

    public LimelightSimulator(List<TargetLocation> targets, Transform2d cameraToRobot, double cameraToTargetHeight, double maxDistance, String limelightTable)
    {
        this(targets, cameraToRobot, cameraToTargetHeight, maxDistance, NetworkTableInstance.getDefault().getTable(limelightTable));
    }

    public LimelightSimulator(List<TargetLocation> targets, Transform2d cameraToRobot, double cameraToTargetHeight, double maxDistance, NetworkTable limelightTable)
    {
        super(targets, cameraToRobot, HORIZONTAL_FOV, maxDistance);

        mCameraToTargetHeight = cameraToTargetHeight;
        mTV = limelightTable.getEntry("tv");
        mTX = limelightTable.getEntry("tx");
        mTY = limelightTable.getEntry("ty");
        mTA = limelightTable.getEntry("ta");

        mTargetArea = 1.0;

        mTV.setNumber(0);
    }

    public void setTargetArea(double targetArea)
    {
        mTargetArea = targetArea;
    }

    public TargetLocation update(Pose2d robotPose)
    {
        TargetLocation bestTarget = null;

        TreeMap<CameraToTargetDelta, TargetLocation> targets = getValidTargets(robotPose);
        if (targets.isEmpty())
        {
            mTV.setNumber(0);
        }
        else
        {
            Map.Entry<CameraToTargetDelta, TargetLocation> bestPair = targets.firstEntry();
            bestTarget = bestPair.getValue();
            double distance = bestPair.getKey().mDistance;

            double el = Math.toDegrees(Math.atan2(mCameraToTargetHeight, distance));
            double az = bestPair.getKey().mDeltaAngle;

            mTV.setNumber(1);
            mTX.setNumber(-az);
            mTY.setNumber(el);
            mTA.setNumber(mTargetArea);
        }

        return bestTarget;
    }
}
