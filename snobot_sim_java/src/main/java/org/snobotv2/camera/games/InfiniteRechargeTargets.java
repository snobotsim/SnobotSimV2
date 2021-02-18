package org.snobotv2.camera.games;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import org.snobotv2.camera.TargetLocation;

import java.util.ArrayList;
import java.util.List;

public final class InfiniteRechargeTargets
{
    private InfiniteRechargeTargets()
    {

    }

    public static List<TargetLocation> getTargets()
    {
        double fieldLongDim = Units.feetToMeters(52) + Units.inchesToMeters(5.25);
        double fieldShortDim = Units.feetToMeters(26) + Units.inchesToMeters(11.25);
        double targetOffset = Units.feetToMeters(5.5);

        List<TargetLocation> targets = new ArrayList<>();

        targets.add(new TargetLocation(new Pose2d(0, fieldShortDim / 2 + targetOffset, Rotation2d.fromDegrees(-180)))
                .setVisibleMinX(0)
                .setVisibleMaxX(fieldLongDim)
                .setName("Left Side"));

        targets.add(new TargetLocation(new Pose2d(fieldLongDim, fieldShortDim / 2 - targetOffset, Rotation2d.fromDegrees(0)))
                .setVisibleMinX(0.0)
                .setVisibleMaxX(fieldLongDim)
                .setName("Right Side"));

                

        return targets;
    }
}
