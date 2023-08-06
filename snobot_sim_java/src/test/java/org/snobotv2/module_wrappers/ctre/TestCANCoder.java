package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import org.junit.jupiter.api.Test;
import org.snobotv2.test_utils.BaseUnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCANCoder extends BaseUnitTest
{
    @Test
    public void testBasic()
    {
        WPI_CANCoder mCanCoder = new WPI_CANCoder(5);

        CtreEncoderSimWrapper mSimWrapper = new CtreEncoderSimWrapper(mCanCoder);

        mSimWrapper.setDistance(0.0);
        assertEquals(0.0, mSimWrapper.getPosition());

        mSimWrapper.setDistance(25.0);
        ctreSimLoop();
        assertEquals(25.0, mSimWrapper.getPosition());

        mSimWrapper.setDistance(34.21);
        ctreSimLoop();
        assertEquals(34.21, mSimWrapper.getPosition());
    }

}
