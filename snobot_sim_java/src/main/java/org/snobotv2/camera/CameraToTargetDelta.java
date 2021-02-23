package org.snobotv2.camera;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        CameraToTargetDelta that = (CameraToTargetDelta) o;
        return Double.compare(that.mDeltaAngle, mDeltaAngle) == 0 && Double.compare(that.mDistance, mDistance) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(mDeltaAngle, mDistance);
    }
}
