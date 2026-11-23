package org.snobotv2.sim_wrappers;

import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.system.RobotController;
import org.wpilib.hardware.motor.MotorController;
import org.wpilib.simulation.ElevatorSim;
import org.snobotv2.interfaces.IDigitalWrapper;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiMotorControllerWrapper;

public class ElevatorSimWrapper extends BaseSingleGearboxSimWrapper
{
    // TODO(pj.reiniger) The "is at lower/upper limit" logic does not work for equals
    private static final double LIMIT_SWITCH_HEIGHT_DIFF = 1e-4;

    private final ElevatorSim mElevatorSim;
    private IDigitalWrapper mLowerLimitSwitch;
    private IDigitalWrapper mUpperLimitSwitch;


    public ElevatorSimWrapper(ElevatorSim elevatorSim, MotorController motor, Encoder encoderWrapper)
    {
        this(elevatorSim, new WpiMotorControllerWrapper(motor), new WpiEncoderWrapper(encoderWrapper));
    }

    public ElevatorSimWrapper(ElevatorSim elevatorSim, IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper)
    {
        super(motor, encoderWrapper);
        mElevatorSim = elevatorSim;
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
        mElevatorSim.setInput(mMotor.getVoltagePercentage() * RobotController.getBatteryVoltage());

        // Next, we update it. The standard loop time is 20ms.
        mElevatorSim.update(mUpdatePeriod);

        // Finally, we set our simulated encoder's readings and simulated battery voltage
        mEncoderWrapper.setDistance(mElevatorSim.getPosition());
        mEncoderWrapper.setVelocity(mElevatorSim.getVelocity());

        mMotor.setCurrent(mElevatorSim.getCurrentDraw());
        mPdpSlots.update(mPdpModule, mElevatorSim.getCurrentDraw());

        boolean atLowerLimit = mElevatorSim.wouldHitLowerLimit(mElevatorSim.getPosition() - LIMIT_SWITCH_HEIGHT_DIFF);
        boolean atUpperLimit = mElevatorSim.wouldHitUpperLimit(mElevatorSim.getPosition() + LIMIT_SWITCH_HEIGHT_DIFF);

        if (mLowerLimitSwitch != null)
        {
            mLowerLimitSwitch.set(atLowerLimit);
        }

        if (mUpperLimitSwitch != null)
        {
            mUpperLimitSwitch.set(atUpperLimit);
        }
    }

}
