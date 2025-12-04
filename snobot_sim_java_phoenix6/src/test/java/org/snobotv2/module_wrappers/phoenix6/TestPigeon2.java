package org.snobotv2.module_wrappers.phoenix6;

import com.ctre.phoenix6.hardware.Pigeon2;
import org.junit.jupiter.api.Test;
import org.snobotv2.interfaces.IGyroWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("removal")
public class TestPigeon2 extends BasePhoenix6UnitTest
{
    private static final double PIGEON_EPSILON = 1e-2;

    @Test
    public void testBasicInline()
    {
        try (Pigeon2 pigeon = new Pigeon2(3))
        {
            pigeon.reset();
            ctreSimLoop();

            runTests(pigeon);
        }
    }

    private void runTests(Pigeon2 pigeon)
    {
        IGyroWrapper wrapper = new Pigeon2Wrapper(pigeon);

        assertEquals(0, pigeon.getRotation2d().getDegrees(), PIGEON_EPSILON);

        wrapper.setAngle(20);
        ctreSimLoop();
        assertEquals(-20, pigeon.getRotation2d().getDegrees(), PIGEON_EPSILON);

        wrapper.setAngle(-43.2);
        ctreSimLoop();
        assertEquals(43.2, pigeon.getRotation2d().getDegrees(), PIGEON_EPSILON);
    }
}
