package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import org.snobotv2.interfaces.IDigitalWrapper;
import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;
import org.snobotv2.module_wrappers.wpi.WpiEncoderWrapper;
import org.snobotv2.module_wrappers.wpi.WpiSpeedControllerWrapper;

public class ElevatorSimWrapper extends BaseSingleGearboxSimWrapper
{
    private final ElevatorSim mElevatorSim;
    private IDigitalWrapper mLowerLimitSwitch;
    private IDigitalWrapper mUpperLimitSwitch;


    public ElevatorSimWrapper(ElevatorSim elevatorSim, SpeedController motor, Encoder encoderWrapper)
    {
        this(elevatorSim, new WpiSpeedControllerWrapper(motor), new WpiEncoderWrapper(encoderWrapper));
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
        mEncoderWrapper.setDistance(mElevatorSim.getPositionMeters());
        mEncoderWrapper.setVelocity(mElevatorSim.getVelocityMetersPerSecond());

        mMotor.setCurrent(mElevatorSim.getCurrentDrawAmps());
        mPdpSlots.update(mPdpModule, mElevatorSim.getCurrentDrawAmps());

        if (mLowerLimitSwitch != null)
        {
            mLowerLimitSwitch.set(mElevatorSim.hasHitLowerLimit());
        }

        if (mUpperLimitSwitch != null)
        {
            mUpperLimitSwitch.set(mElevatorSim.hasHitUpperLimit());
        }
    }

}
