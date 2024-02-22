package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SimableCANSparkFlex;
import org.junit.jupiter.api.Test;
import org.snobotv2.sim_wrappers.InstantaneousMotorSim;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRevFlexEncoder
{
    @Test
    public void testEncoder()
    {
        try (SimableCANSparkFlex sparkFlex = new SimableCANSparkFlex(1, CANSparkLowLevel.MotorType.kBrushless))
        {
            RelativeEncoder encoder = sparkFlex.getEncoder();

            InstantaneousMotorSim motorSim = new InstantaneousMotorSim(
                    new RevMotorControllerSimWrapper(sparkFlex),
                    RevEncoderSimWrapper.create(sparkFlex),
                    10);

            for (int i = 0; i < 100; ++i)
            {
                sparkFlex.set(0.5);
                motorSim.update();
            }

            assertEquals(10, encoder.getPosition(), 0.001);
            assertEquals(5, encoder.getVelocity(), 0.001);
        }
    }
}
