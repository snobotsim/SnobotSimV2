package org.snobotv2.camera;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseCameraSimulatorTest
{
    @Test
    public void doubleTargetTest()
    {
        List<TargetLocation> targets = new ArrayList<>();
        targets.add(new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(0))));
        targets.add(new TargetLocation(new Pose2d(10, 20, Rotation2d.fromDegrees(0))));

        BaseCameraSimulator sim = new BaseCameraSimulator(targets, 50, Double.MAX_VALUE);

        TreeMap<CameraToTargetDelta, TargetLocation> visibleTargets;
        Map.Entry<CameraToTargetDelta, TargetLocation> firstEntry;
        Map.Entry<CameraToTargetDelta, TargetLocation> secondEntry;

        // Start at origin. Should only see target 1
        visibleTargets = sim.getValidTargets(new Pose2d(0, 0, Rotation2d.fromDegrees(0)));
        firstEntry = visibleTargets.firstEntry();
        assertEquals(1, visibleTargets.size());
        assertEquals(Math.sqrt(200), firstEntry.getKey().mDistance);
        assertEquals(10, firstEntry.getValue().getPosition().getY());


        // In front of first target, should see both
        visibleTargets = sim.getValidTargets(new Pose2d(0, 10, Rotation2d.fromDegrees(0)));
        firstEntry = visibleTargets.firstEntry();
        secondEntry = visibleTargets.lastEntry();
        assertEquals(2, visibleTargets.size());
        assertEquals(Math.sqrt(100), firstEntry.getKey().mDistance);
        assertEquals(10, firstEntry.getValue().getPosition().getY());
        assertEquals(Math.sqrt(200), secondEntry.getKey().mDistance);
        assertEquals(20, secondEntry.getValue().getPosition().getY());

        // In front of Second target, should see both
        visibleTargets = sim.getValidTargets(new Pose2d(0, 20, Rotation2d.fromDegrees(0)));
        firstEntry = visibleTargets.firstEntry();
        secondEntry = visibleTargets.lastEntry();
        assertEquals(2, visibleTargets.size());
        assertEquals(Math.sqrt(100), firstEntry.getKey().mDistance);
        assertEquals(20, firstEntry.getValue().getPosition().getY());
        assertEquals(Math.sqrt(200), secondEntry.getKey().mDistance);
        assertEquals(10, secondEntry.getValue().getPosition().getY());
    }
}
