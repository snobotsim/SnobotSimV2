package com.revrobotics;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

public class SimableCANSparkFlex extends CANSparkFlex implements SimableRevDevice
{
    protected final SparkSimHelper mSimHelper;

    /**
     * Create a new object to control a SPARK Flex motor Controller
     *
     * @param deviceId The device ID.
     * @param type     The motor type connected to the controller. Brushless motor wires must be connected
     *                 to their matching colors and the hall sensor must be plugged in. Brushed motors must be
     *                 connected to the Red and Black terminals only.
     */
    public SimableCANSparkFlex(int deviceId, MotorType type)
    {
        super(deviceId, type);

        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + deviceId + "]");
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
