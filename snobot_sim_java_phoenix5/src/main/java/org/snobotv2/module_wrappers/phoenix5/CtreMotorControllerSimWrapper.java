package org.snobotv2.module_wrappers.phoenix5;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.RobotController;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

import java.util.function.DoubleConsumer;

public class CtreMotorControllerSimWrapper extends BaseMotorControllerWrapper
{
    private final DoubleConsumer mBusVoltageSetter;

    public CtreMotorControllerSimWrapper(WPI_TalonSRX motorController)
    {
        super(motorController.getDeviceID(), motorController::getMotorOutputPercent);
        mBusVoltageSetter = voltage -> motorController.getSimCollection().setBusVoltage(voltage);
    }

    public CtreMotorControllerSimWrapper(WPI_TalonFX motorController)
    {
        super(motorController.getDeviceID(), motorController::getMotorOutputPercent);
        mBusVoltageSetter = voltage -> motorController.getSimCollection().setBusVoltage(voltage);
    }

    @Override
    public void update()
    {
        mBusVoltageSetter.accept(RobotController.getBatteryVoltage());
    }

    @Override
    public void setCurrent(double current)
    {
//        mTalon.getSimCollection().setStatorCurrent(current);
//        mTalon.getSimCollection().setSupplyCurrent(current);
    }
}
