package org.snobotv2.camera;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimelightSimulatorTest
{
    @Test
    public void bestTargetTest()
    {
        List<TargetLocation> targets = new ArrayList<>();
        targets.add(new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(0))));
        targets.add(new TargetLocation(new Pose2d(10, 20, Rotation2d.fromDegrees(0))));

        try (NetworkTableInstance testInstance = NetworkTableInstance.create())
        {
            NetworkTable networkTable = testInstance.getTable("limelight");
            NetworkTableEntry visible = networkTable.getEntry("tv");
            NetworkTableEntry tx = networkTable.getEntry("tx");
            NetworkTableEntry ty = networkTable.getEntry("ty");

            LimelightSimulator sim = new LimelightSimulator(targets, Units.feetToMeters(10), Double.MAX_VALUE, networkTable);

            testInstance.flush();
            assertEquals(0.0, visible.getNumber(-1));

            sim.update(new Pose2d(0, 0, Rotation2d.fromDegrees(0)));
            testInstance.flush();
            assertEquals(1.0, visible.getNumber(-1));
            assertEquals(45.0, tx.getNumber(-100));
            assertEquals(12.16269, ty.getNumber(-100).doubleValue(), 1e-3);


            sim.update(new Pose2d(0, 10, Rotation2d.fromDegrees(0)));
            testInstance.flush();
            assertEquals(1.0, visible.getNumber(-1));
            assertEquals(0.0, tx.getNumber(-100));
            assertEquals(16.95122, ty.getNumber(-100).doubleValue(), 1e-3);
        }
    }
}
