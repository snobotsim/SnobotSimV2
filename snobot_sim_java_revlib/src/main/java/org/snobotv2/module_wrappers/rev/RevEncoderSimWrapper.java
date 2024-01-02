package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.snobotv2.module_wrappers.BaseEncoderWrapper;

public final class RevEncoderSimWrapper extends BaseEncoderWrapper
{
    public static RevEncoderSimWrapper create(CANSparkMax motorController)
    {
        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + motorController.getDeviceId() + "]");
        SimDouble position = deviceSim.getDouble("Position");
        SimDouble velocity = deviceSim.getDouble("Velocity");

        return new RevEncoderSimWrapper(position, velocity);
    }

    private RevEncoderSimWrapper(SimDouble position, SimDouble velocity)
    {
        super(
                position::get,
                position::set,
                velocity::set);
    }
}
