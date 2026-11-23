package org.snobotv2.sim_wrappers;

import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.math.util.Units;
import org.wpilib.system.RobotController;
import org.wpilib.hardware.motor.MotorController;
import org.wpilib.simulation.FlywheelSim;
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
        mEncoderWrapper.setVelocity(Units.radiansPerSecondToRotationsPerMinute(mFlywheelSim.getAngularVelocity()));

        mMotor.setCurrent(mFlywheelSim.getCurrentDraw());
        mPdpSlots.update(mPdpModule, mFlywheelSim.getCurrentDraw());
    }
}
