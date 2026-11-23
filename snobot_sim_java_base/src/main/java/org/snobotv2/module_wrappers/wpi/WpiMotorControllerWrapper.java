package org.snobotv2.module_wrappers.wpi;

import org.wpilib.hardware.motor.PWMMotorController;
import org.wpilib.hardware.motor.MotorController;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class WpiMotorControllerWrapper extends BaseMotorControllerWrapper
{
    public WpiMotorControllerWrapper(PWMMotorController motorController)
    {
        super(motorController.getChannel(), motorController::getThrottle);
    }

    public WpiMotorControllerWrapper(MotorController motorController)
    {
        super(-1, motorController::getThrottle);
    }
}
