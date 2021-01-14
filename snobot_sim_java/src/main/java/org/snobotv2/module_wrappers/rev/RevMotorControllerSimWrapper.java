package org.snobotv2.module_wrappers.rev;

import com.revrobotics.SimableCANSparkMax;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class RevMotorControllerSimWrapper extends BaseMotorControllerWrapper
{
    private final SimableCANSparkMax mMotorController;
    private final SimDouble mBusVoltage;
    private final SimDouble mMotorCurrent;

    public RevMotorControllerSimWrapper(SimableCANSparkMax motorController)
    {
        super(motorController.getDeviceId(), motorController::getAppliedOutput);

        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + motorController.getDeviceId() + "]");
        mBusVoltage = deviceSim.getDouble("Bus Voltage");
        mMotorCurrent = deviceSim.getDouble("Motor Current");

        mMotorController = motorController;
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
