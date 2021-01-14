package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.RobotController;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class CtreMotorControllerSimWrapper extends BaseMotorControllerWrapper
{
    private final WPI_TalonSRX mTalon;

    public CtreMotorControllerSimWrapper(WPI_TalonSRX motorController)
    {
        super(motorController.getDeviceID(), motorController::getMotorOutputPercent);
        mTalon = motorController;
    }

    @Override
    public void update()
    {
        mTalon.getSimCollection().setBusVoltage(RobotController.getBatteryVoltage());
    }

    @Override
    public void setCurrent(double current)
    {
//        mTalon.getSimCollection().setStatorCurrent(current);
//        mTalon.getSimCollection().setSupplyCurrent(current);
    }
}
