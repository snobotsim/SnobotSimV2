package org.snobotv2.module_wrappers.rev;

import com.revrobotics.CANSparkBase;
import com.revrobotics.SimableRevDevice;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class RevMotorControllerSimWrapper extends BaseMotorControllerWrapper
{
    private final SimableRevDevice mMotorController;
    private final SimDouble mBusVoltage;
    private final SimDouble mMotorCurrent;

    public RevMotorControllerSimWrapper(CANSparkBase motorController)
    {
        super(motorController.getDeviceId(), motorController::getAppliedOutput);
        if (!(motorController instanceof SimableRevDevice))
        {
            throw new IllegalArgumentException("The provided motor controller is not simmable!");
        }

        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + motorController.getDeviceId() + "]");
        mBusVoltage = deviceSim.getDouble("Bus Voltage");
        mMotorCurrent = deviceSim.getDouble("Motor Current");

        mMotorController = (SimableRevDevice) motorController;
    }

    @Override
    public void update()
    {
        mMotorController.updateSim();
        mBusVoltage.set(RobotController.getBatteryVoltage());
    }

    @Override
    public void setCurrent(double current)
    {
        mMotorCurrent.set(current);
    }
}
