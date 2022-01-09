package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.snobotv2.interfaces.IGyroWrapper;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPigeonImu extends BaseUnitTest
{
    private static final double PIGEON_EPSILON = 1e-2;

    @Test
    public void testBasicInline()
    {
        try (WPI_PigeonIMU pigeon = new WPI_PigeonIMU(3))
        {
            runTests(pigeon);
        }
    }

    @Disabled("Ribbon cable pigeons have a sim bug in the CTRE library")
    @Test
    public void testBasicAttached()
    {
        try (WPI_TalonSRX talon = new WPI_TalonSRX(1);
             WPI_PigeonIMU pigeon = new WPI_PigeonIMU(talon))
        {
            runTests(pigeon);
        }

    }

    private void runTests(WPI_PigeonIMU pigeon)
    {
        IGyroWrapper wrapper = new CtrePigeonImuWrapper(pigeon);

        assertEquals(0, pigeon.getAngle(), PIGEON_EPSILON);

        wrapper.setAngle(20);
        ctreSimLoop();
        assertEquals(20, pigeon.getAngle(), PIGEON_EPSILON);

        wrapper.setAngle(-43.2);
        ctreSimLoop();
        assertEquals(-43.2, pigeon.getAngle(), PIGEON_EPSILON);
    }
}
