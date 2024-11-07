package org.snobotv2.module_wrappers.rev;

import com.revrobotics.sim.SparkFlexSim;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import edu.wpi.first.math.system.plant.DCMotor;
import org.snobotv2.module_wrappers.BaseEncoderWrapper;

public final class RevEncoderSimWrapper extends BaseEncoderWrapper
{
    public static RevEncoderSimWrapper create(SparkBase spark)
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

        return new RevEncoderSimWrapper(sparkSim, encoderSim);
    }

    private RevEncoderSimWrapper(SparkSim sparkSim, SparkRelativeEncoderSim encoderSim)
    {
        super(
                sparkSim::getPosition,
                pos ->
                {
                    sparkSim.setPosition(pos);
                    encoderSim.setPosition(pos);
                },
                vel ->
                {
                    sparkSim.setVelocity(vel);
                    encoderSim.setVelocity(vel);
                });
    }
}
