package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.snobotv2.interfaces.IGyroWrapper;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPigeon2 extends BaseUnitTest
{
    private static final double PIGEON_EPSILON = 1e-2;

    @Test
    public void testBasicInline()
    {
        try (WPI_Pigeon2 pigeon = new WPI_Pigeon2(3))
        {
            runTests(pigeon);
        }
    }

    private void runTests(WPI_Pigeon2 pigeon)
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
