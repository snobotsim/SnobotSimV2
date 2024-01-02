package org.snobotv2.camera;

import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimelightSimulatorTest
{
    @SuppressWarnings("PMD.ShortVariable")
    @Test
    public void bestTargetTest()
    {
        List<TargetLocation> targets = new ArrayList<>();
        targets.add(new TargetLocation(new Pose2d(10, 10, Rotation2d.fromDegrees(0))));
        targets.add(new TargetLocation(new Pose2d(10, 20, Rotation2d.fromDegrees(0))));

        try (NetworkTableInstance testInstance = NetworkTableInstance.create())
        {
            NetworkTable networkTable = testInstance.getTable("limelight");
            try (NetworkTableEntry visible = networkTable.getEntry("tv");
                NetworkTableEntry tx = networkTable.getEntry("tx");
                NetworkTableEntry ty = networkTable.getEntry("ty"))
            {
                LimelightSimulator sim = new LimelightSimulator(targets, new Transform2d(), Units.feetToMeters(10), Double.MAX_VALUE, networkTable);

                testInstance.flush();
                assertEquals(0.0, visible.getNumber(-1));

                // Out of FOV
                sim.update(new Pose2d(0, 0, Rotation2d.fromDegrees(0)));
                testInstance.flush();
                assertEquals(0.0, visible.getNumber(-1));

                sim.update(new Pose2d(0, 0, Rotation2d.fromDegrees(40)));
                testInstance.flush();
                assertEquals(1.0, visible.getNumber(-1));
                assertEquals(-5.0, tx.getNumber(-100).doubleValue(), 1e-6);
                assertEquals(12.162_69, ty.getNumber(-100).doubleValue(), 1e-3);

                sim.update(new Pose2d(0, 10, Rotation2d.fromDegrees(0)));
                testInstance.flush();
                assertEquals(1.0, visible.getNumber(-1));
                assertEquals(0.0, tx.getNumber(-100).doubleValue(), 1e-6);
                assertEquals(16.951_22, ty.getNumber(-100).doubleValue(), 1e-3);
            }
        }
    }
}
