package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiMotorControllerWrapper;

public class FlywheelSimWrapper extends BaseSingleGearboxSimWrapper
{
    private final FlywheelSim mFlywheelSim;

    public FlywheelSimWrapper(FlywheelSim flywheelSim, MotorController motor, Encoder encoderWrapper)
    {
        this(flywheelSim, new WpiMotorControllerWrapper(motor), new WpiEncoderWrapper(encoderWrapper));
    }

    public FlywheelSimWrapper(FlywheelSim flywheelSim, IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper)
    {
        super(motor, encoderWrapper);
        mFlywheelSim = flywheelSim;
    }

    @Override
    public void update()
    {
        mMotor.update();

        // In this method, we update our simulation of what our elevator is doing
        // First, we set our "inputs" (voltages)
        mFlywheelSim.setInput(mMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());

        // Next, we update it. The standard loop time is 20ms.
        mFlywheelSim.update(mUpdatePeriod);

        // Finally, we set our simulated encoder's readings and simulated battery voltage
        mEncoderWrapper.setVelocity(mFlywheelSim.getAngularVelocityRPM());

        mMotor.setCurrent(mFlywheelSim.getCurrentDrawAmps());
        mPdpSlots.update(mPdpModule, mFlywheelSim.getCurrentDrawAmps());
    }
}
