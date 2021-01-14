package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiSpeedControllerWrapper;

public class SingleJointedArmSimWrapper extends BaseSingleGearboxSimWrapper
{
    private final SingleJointedArmSim mArmSim;

    public SingleJointedArmSimWrapper(SingleJointedArmSim armSim, SpeedController motor, Encoder encoderWrapper)
    {
        this(armSim, new WpiSpeedControllerWrapper(motor), new WpiEncoderWrapper(encoderWrapper));
    }

    public SingleJointedArmSimWrapper(SingleJointedArmSim armSim, IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper)
    {
        super(motor, encoderWrapper);
        mArmSim = armSim;
    }

    @Override
    public void update()
    {
        // In this method, we update our simulation of what our elevator is doing
        // First, we set our "inputs" (voltages)
        mArmSim.setInput(mMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());

        // Next, we update it. The standard loop time is 20ms.
        mArmSim.update(mUpdatePeriod);

        // Finally, we set our simulated encoder's readings and simulated battery voltage
        mEncoderWrapper.setDistance(mArmSim.getAngleRads());

        mMotor.setCurrent(mArmSim.getCurrentDrawAmps());
        mPdpSlots.update(mPdpModule, mArmSim.getCurrentDrawAmps());
    }
}
