package org.snobotv2.camera;

public class CameraToTargetDelta implements Comparable<CameraToTargetDelta>
{
    public final double mDeltaAngle;
    public final double mDistance;

    public CameraToTargetDelta(double distance, double angleDelta)
    {
        mDistance = distance;
        mDeltaAngle = angleDelta;
    }

    @Override
    public int compareTo(CameraToTargetDelta other)
    {
        int result = Double.compare(mDistance, other.mDistance);
        if (result != 0)
        {
            return result;
        }

        return Double.compare(mDeltaAngle, other.mDeltaAngle);
    }
}
