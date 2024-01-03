package com.revrobotics;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

@SuppressWarnings("PMD.TooManyMethods")
public class SimableCANSparkMax extends CANSparkMax implements SimableRevDevice
{
    protected final SparkSimHelper mSimHelper;

    /**
     * Create a new SPARK MAX Controller
     *
     * @param deviceID The device ID.
     * @param type     The motor type connected to the controller. Brushless motors
     *                 must be connected to their matching color and the hall sensor
     *                 plugged in. Brushed motors must be connected to the Red and
     */
    public SimableCANSparkMax(int deviceID, MotorType type)
    {
        super(deviceID, type);

        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + deviceID + "]");
        mSimHelper = new SparkSimHelper(deviceSim, this, sparkMaxHandle);

    }

    @Override
    public void updateSim()
    {
        mSimHelper.updateSim();
    }

    //////////////////////////////////////////////////
    // Base class hijacking

    @Override
    public REVLibError follow(final CANSparkBase leader)
    {
        if (RobotBase.isReal())
        {
            return super.follow(leader);
        }

        return mSimHelper.follow(leader);
    }

    @Override
    public SparkPIDController getPIDController()
    {
        if (RobotBase.isReal())
        {
            return super.getPIDController();
        }

        return mSimHelper.getPIDController();
    }

    @Override
    /* default */ REVLibError setpointCommand(double value, ControlType ctrl, int pidSlot, double arbFeedforward, int arbFFUnits)
    {
        if (RobotBase.isReal())
        {
            return super.setpointCommand(value, ctrl, pidSlot, arbFeedforward, arbFFUnits);
        }

        return mSimHelper.setpointCommand(value, ctrl, pidSlot, arbFeedforward, arbFFUnits);
    }

    @Override
    public RelativeEncoder getEncoder(SparkRelativeEncoder.Type encoderType, int countsPerRev)
    {
        return mSimHelper.setLatchedEncoder(super.getEncoder(encoderType, countsPerRev));
    }

    //////////////////////////////////////////////////

}
