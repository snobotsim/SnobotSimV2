package org.snobotv2.module_wrappers.wpi;

import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.SpeedController;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class WpiSpeedControllerWrapper extends BaseMotorControllerWrapper
{
    public WpiSpeedControllerWrapper(PWMSpeedController speedController)
    {
        super(speedController.getChannel(), speedController::get);
    }

    public WpiSpeedControllerWrapper(SpeedController speedController)
    {
        super(-1, speedController::get);
    }
}
