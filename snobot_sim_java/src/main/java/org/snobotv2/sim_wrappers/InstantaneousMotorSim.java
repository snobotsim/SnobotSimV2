package org.snobotv2.sim_wrappers;

import org.snobotv2.interfaces.IEncoderWrapper;
import org.snobotv2.interfaces.IMotorControllerWrapper;

public class InstantaneousMotorSim extends BaseSingleGearboxSimWrapper
{
    private final double mMaxSpeed;
    private double mPosition;

    public InstantaneousMotorSim(IMotorControllerWrapper motor, IEncoderWrapper encoderWrapper, double maxSpeed)
    {
        super(motor, encoderWrapper);
        mMaxSpeed = maxSpeed;
    }

    @Override
    public void update()
    {
        mMotor.update();

        double velocity = mMotor.getVoltagePercentage() * mMaxSpeed;
        mPosition += velocity * mUpdatePeriod;

        mEncoderWrapper.setDistance(mPosition);
        mEncoderWrapper.setVelocity(velocity);
    }
}
