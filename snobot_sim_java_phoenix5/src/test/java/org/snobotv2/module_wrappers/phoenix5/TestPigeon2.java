package org.snobotv2.module_wrappers.phoenix5;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import org.junit.jupiter.api.Test;
import org.snobotv2.interfaces.IGyroWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("removal")
public class TestPigeon2 extends BasePhoenix5UnitTest
{
    private static final double PIGEON_EPSILON = 1e-2;

    @Test
    public void testBasicInline()
    {
        try (WPI_Pigeon2 pigeon = new WPI_Pigeon2(3))
        {
            pigeon.reset();
            ctreSimLoop();

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
