package com.revrobotics;

public class SimableCANSparkFlex extends CANSparkFlex implements SimableRevDevice
{
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
    }

    @Override
    public void updateSim()
    {
        throw new UnsupportedOperationException("Unsupported");
    }
}
