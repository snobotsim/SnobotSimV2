package org.snobotv2.camera;

import edu.wpi.first.math.geometry.Pose2d;

import java.util.List;
import java.util.TreeMap;

public class BaseCameraSimulator
{
    private final List<TargetLocation> mTargets;
    private final double mHorizontalFov;
    private final double mMaxDistance;

    public BaseCameraSimulator(List<TargetLocation> targets, double cameraFov, double maxDistance)
    {
        mTargets = targets;
        mHorizontalFov = cameraFov;
        mMaxDistance = maxDistance;
    }

    public TreeMap<CameraToTargetDelta, TargetLocation> getValidTargets(Pose2d pose) // NOPMD
    {
        TreeMap<CameraToTargetDelta, TargetLocation> valid = new TreeMap<>();

        for (TargetLocation target : mTargets)
        {
            CameraToTargetDelta delta = target.isInVisiblePosition(pose, mHorizontalFov, mMaxDistance);
            if (delta != null)
            {
                valid.put(delta, target);
            }
        }

        return valid;
    }
}
