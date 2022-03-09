package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import org.snobotv2.interfaces.IDigitalWrapper;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiMotorControllerWrapper;

public class SingleJointedArmSimWrapper extends BaseSingleGearboxSimWrapper
{
    private final SingleJointedArmSim mArmSim;
    private IDigitalWrapper mLowerLimitSwitch;
    private IDigitalWrapper mUpperLimitSwitch;
    private final boolean mUseDegrees;

    public SingleJointedArmSimWrapper(SingleJointedArmSim armSim, MotorController motor, Encoder encoderWrapper)
    {
        this(armSim, new WpiMotorControllerWrapper(motor), new WpiEncoderWrapper(encoderWrapper));
    }

    public SingleJointedArmSimWrapper(SingleJointedArmSim armSim, IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper)
    {
        this(armSim, motor, encoderWrapper, false);
    }

    public SingleJointedArmSimWrapper(SingleJointedArmSim armSim, IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper, boolean useDegrees)
    {
        super(motor, encoderWrapper);
        mArmSim = armSim;
        mUseDegrees = useDegrees;
    }

    public void setLowerLimitSwitch(IDigitalWrapper limitSwitch)
    {
        mLowerLimitSwitch = limitSwitch;
    }

    public void setUpperLimitSwitch(IDigitalWrapper limitSwitch)
    {
        mUpperLimitSwitch = limitSwitch;
    }

    @Override
    public void update()
    {
        mMotor.update();

        // In this method, we update our simulation of what our elevator is doing
        // First, we set our "inputs" (voltages)
        mArmSim.setInput(mMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());

        // Next, we update it. The standard loop time is 20ms.
        mArmSim.update(mUpdatePeriod);


        // Finally, we set our simulated encoder's readings and simulated battery voltage
        double position = mArmSim.getAngleRads();
        double velocity = mArmSim.getVelocityRadPerSec();
        if (mUseDegrees)
        {
            position = Math.toDegrees(position);
            velocity = Math.toDegrees(velocity);
        }

        mEncoderWrapper.setDistance(position);
        mEncoderWrapper.setVelocity(velocity);

        mMotor.setCurrent(mArmSim.getCurrentDrawAmps());
        mPdpSlots.update(mPdpModule, mArmSim.getCurrentDrawAmps());

        if (mLowerLimitSwitch != null)
        {
            mLowerLimitSwitch.set(mArmSim.hasHitLowerLimit());
        }

        if (mUpperLimitSwitch != null)
        {
            mUpperLimitSwitch.set(mArmSim.hasHitUpperLimit());
        }
    }
}
