package org.snobotv2.camera;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TargetLocationTest
{
    @SuppressWarnings("PMD.ShortVariable")
    private Pose2d pose(double x, double y, double angle)
    {
        return new Pose2d(x, y, Rotation2d.fromDegrees(angle));
    }

    @Test
    @SuppressWarnings("PMD")
    public void testLimitlessTarget()
    {
        double fov = 40;
        double maxDistance = Double.MAX_VALUE;

        TargetLocation location = new TargetLocation(new Pose2d(0, 0, null));

        for (double x : new double[]{-100, 0, 100})
        {
            for (double y : new double[]{-100, 0, 100})
            {
                for (double angle : new double[]{-179, -90, 0, 90, 179})
                {
                    assertNotNull(location.isInVisiblePosition(pose(x, y, angle), fov, maxDistance));
                }
            }
        }
    }

    @Test
    public void testLimitedX()
    {
        double fov = 40;
        double maxDistance = Double.MAX_VALUE;

        TargetLocation location = new TargetLocation(new Pose2d(0, 0, null))
                .setVisibleMinX(-20)
                .setVisibleMaxX(30);

        assertNotNull(location.isInVisiblePosition(pose(0, 0, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 100, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(-10, 50, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(-20, 50, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(17, 50, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(30, 50, 0), fov, maxDistance));


        assertNull(location.isInVisiblePosition(pose(-30, 50, 0), fov, maxDistance));
        assertNull(location.isInVisiblePosition(pose(31, 50, 0), fov, maxDistance));
    }


    @Test
    public void testInTargetWithAngle0InFov()
    {
        double fov = 40;
        double maxDistance = Double.MAX_VALUE;

        TargetLocation location = new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(0)));

        // Robot directly in front of the target. The robots angle should be the only thing that matters
        assertNull(location.isInVisiblePosition(pose(0, 10, -45), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 10, -40), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 10, -20), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 10, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 10, 20), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 10, 40), fov, maxDistance));
        assertNull(location.isInVisiblePosition(pose(0, 10, 45), fov, maxDistance));

        assertNull(location.isInVisiblePosition(pose(0, 0, 0), fov, maxDistance));
        assertNotNull(location.isInVisiblePosition(pose(0, 0, 45), fov, maxDistance));
        assertNull(location.isInVisiblePosition(pose(0, 0, 90), fov, maxDistance));
    }

    @Test
    public void testInTargetWithAngle180InFov()
    {
        double fov = 40;
        double maxDistance = Double.MAX_VALUE;

        TargetLocation location45 = new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(180)));
        assertNotNull(location45.isInVisiblePosition(pose(0, 10, 0), fov, maxDistance));
        assertNull(location45.isInVisiblePosition(pose(0, 10, 160), fov, maxDistance));
        assertNull(location45.isInVisiblePosition(pose(0, 10, 200), fov, maxDistance));
        assertNull(location45.isInVisiblePosition(pose(0, 10, -179), fov, maxDistance));
    }

    @Test
    public void testFovWraparound()
    {
        double fov = 40;
        double maxDistance = Double.MAX_VALUE;

        TargetLocation locationZero = new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(0)));
        assertNotNull(locationZero.isInVisiblePosition(pose(0, 10, 20 - 360 * 4), fov, maxDistance));
        assertNotNull(locationZero.isInVisiblePosition(pose(0, 10, 20 + 360 * 4), fov, maxDistance));

        TargetLocation location45 = new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(45)));
        assertNotNull(location45.isInVisiblePosition(pose(0, 10, 20 - 360 * 4), fov, maxDistance));
        assertNotNull(location45.isInVisiblePosition(pose(0, 10, 20 + 360 * 4), fov, maxDistance));
    }
}
