package org.snobotv2.camera;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform2d;

import java.util.List;
import java.util.TreeMap;

public class BaseCameraSimulator
{
    private final Transform2d mCameraToRobot;
    private final List<TargetLocation> mTargets;
    private final double mHorizontalFov;
    private final double mMaxDistance;

    public BaseCameraSimulator(List<TargetLocation> targets, Transform2d cameraToRobot, double cameraFov, double maxDistance)
    {
        mTargets = targets;
        mHorizontalFov = cameraFov;
        mMaxDistance = maxDistance;
        mCameraToRobot = cameraToRobot;
    }

    public TreeMap<CameraToTargetDelta, TargetLocation> getValidTargets(Pose2d robotPose) // NOPMD
    {
        Pose2d cameraPose = robotPose.transformBy(mCameraToRobot.inverse());

        TreeMap<CameraToTargetDelta, TargetLocation> valid = new TreeMap<>(); // NOPMD

        for (TargetLocation target : mTargets)
        {
            CameraToTargetDelta delta = target.isInVisiblePosition(cameraPose, mHorizontalFov, mMaxDistance);
            if (delta != null)
            {
                valid.put(delta, target);
            }
        }

        return valid;
    }
}
