package org.snobotv2.camera.games;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import org.snobotv2.camera.TargetLocation;

import java.util.ArrayList;
import java.util.List;

public final class RapidReactTargets
{
    private RapidReactTargets()
    {
    }

    public static List<TargetLocation> getTargets()
    {
        double fieldLongDim = Units.feetToMeters(54);
        double fieldShortDim = Units.feetToMeters(27);

        List<TargetLocation> targets = new ArrayList<>();

        targets.add(new TargetLocation(new Pose2d(fieldLongDim / 2, fieldShortDim / 2, Rotation2d.fromDegrees(0)))
                .setName("Hub"));


        return targets;
    }
}
