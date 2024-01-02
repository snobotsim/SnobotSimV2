package org.snobotv2.module_wrappers.wpi;

import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class WpiMotorControllerWrapper extends BaseMotorControllerWrapper
{
    public WpiMotorControllerWrapper(PWMMotorController motorController)
    {
        super(motorController.getChannel(), motorController::get);
    }

    public WpiMotorControllerWrapper(MotorController motorController)
    {
        super(-1, motorController::get);
    }
}
