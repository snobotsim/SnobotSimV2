package org.snobotv2.module_wrappers.rev;

import com.revrobotics.sim.SparkFlexSim;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import org.wpilib.math.system.DCMotor;
import org.snobotv2.module_wrappers.BaseEncoderWrapper;

public final class RevEncoderSimWrapper extends BaseEncoderWrapper
{
    public static RevEncoderSimWrapper create(SparkBase spark)
    {
        return create(spark, 1.0, 1.0);
    }

    /**
     * Creates a wrapper for a SPARK's built in encoder.
     *
     * REVLib 2027 no longer supports conversion factors on the device, so the encoder always reports native units
     * (rotations and RPM). The simulator works in the robot code's units, for example meters, so the factors given
     * here are used to convert the simulated values back into native units.
     *
     * @param spark The SPARK to simulate
     * @param positionConversionFactor The number of robot code position units per motor rotation
     * @param velocityConversionFactor The number of robot code velocity units per motor RPM
     * @return The wrapper
     */
    public static RevEncoderSimWrapper create(SparkBase spark, double positionConversionFactor, double velocityConversionFactor)
    {
        SparkSim sparkSim;
        SparkRelativeEncoderSim encoderSim;
        if (spark instanceof SparkMax)
        {
            sparkSim = new SparkMaxSim((SparkMax) spark, DCMotor.getNEO(1));
            encoderSim = new SparkRelativeEncoderSim((SparkMax) spark);
        }
        else if (spark instanceof SparkFlex)
        {
            sparkSim = new SparkFlexSim((SparkFlex) spark, DCMotor.getNEO(1));
            encoderSim = new SparkRelativeEncoderSim((SparkFlex) spark);
        }
        else
        {
            throw new IllegalArgumentException("The provided motor controller is not simmable!");
        }

        return new RevEncoderSimWrapper(sparkSim, encoderSim, positionConversionFactor, velocityConversionFactor);
    }

    private RevEncoderSimWrapper(SparkSim sparkSim, SparkRelativeEncoderSim encoderSim, double positionConversionFactor, double velocityConversionFactor)
    {
        super(
                () -> sparkSim.getPosition() * positionConversionFactor,
                pos ->
                {
                    sparkSim.setPosition(pos / positionConversionFactor);
                    encoderSim.setPosition(pos / positionConversionFactor);
                },
                vel ->
                {
                    sparkSim.setVelocity(vel / velocityConversionFactor);
                    encoderSim.setVelocity(vel / velocityConversionFactor);
                });
    }
}
