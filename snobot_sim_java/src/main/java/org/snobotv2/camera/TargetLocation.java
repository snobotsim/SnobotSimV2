package org.snobotv2.camera;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Transform2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class TargetLocation
{
    private final Pose2d mPosition;

    private String mName;
    private double mVisibleMinX;
    private double mVisibleMinY;
    private double mVisibleMaxX;
    private double mVisibleMaxY;

    public TargetLocation(Pose2d pose)
    {
        mPosition = pose;
        mName = "";

        mVisibleMinX = -Double.MAX_VALUE;
        mVisibleMinY = -Double.MAX_VALUE;
        mVisibleMaxX = Double.MAX_VALUE;
        mVisibleMaxY = Double.MAX_VALUE;
    }

    public TargetLocation setName(String name)
    {
        mName = name;
        return this;
    }

    public TargetLocation setVisibleMinX(double minX)
    {
        mVisibleMinX = minX;
        return this;
    }

    public TargetLocation setVisibleMaxX(double maxX)
    {
        mVisibleMaxX = maxX;
        return this;
    }

    public TargetLocation setVisibleMinY(double minY)
    {
        mVisibleMinY = minY;
        return this;
    }

    public TargetLocation setVisibleMaxY(double maxY)
    {
        mVisibleMaxY = maxY;
        return this;
    }


    public CameraToTargetDelta isInVisiblePosition(Pose2d robotPose, double horizontalFov, double maxDistance)
    {
        // Alias for readability
        double robotX = robotPose.getX();
        double robotY = robotPose.getY();
        double angleDelta = 0;

        if (robotX > mVisibleMaxX || robotX < mVisibleMinX)
        {
            return null;
        }

        if (robotY > mVisibleMaxY || robotY < mVisibleMinY)
        {
            return null;
        }

        double dx = robotPose.getX() - mPosition.getX();
        double dy = robotPose.getY() - mPosition.getY();

        if (mPosition.getRotation() != null)
        {
            Transform2d diff = mPosition.minus(robotPose);
            Translation2d trans = diff.getTranslation();

            Rotation2d angleFromRobot = new Rotation2d(trans.getX(), trans.getY());
            angleDelta = angleFromRobot.getDegrees();

            if (Math.abs(angleDelta) > horizontalFov)
            {
                return null;
            }
        }

        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > maxDistance)
        {
            return null;
        }

        return new CameraToTargetDelta(distance, angleDelta);
    }


    public Pose2d getPosition()
    {
        return mPosition;
    }

    @Override
    public String toString()
    {
        return "TargetLocation [mName=" + mName + ", mPosition=" + mPosition + "]";
    }
}
