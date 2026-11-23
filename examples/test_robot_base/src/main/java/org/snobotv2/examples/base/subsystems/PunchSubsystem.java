package org.snobotv2.examples.base.subsystems;

import org.wpilib.hardware.discrete.AnalogInput;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.pneumatic.DoubleSolenoid;
import org.wpilib.hardware.pneumatic.PneumaticsModuleType;
import org.wpilib.command2.SubsystemBase;

public class PunchSubsystem extends SubsystemBase implements AutoCloseable
{
    private final DoubleSolenoid mSolenoid;
    private final AnalogInput mPressureSensor;

    public PunchSubsystem()
    {
        mSolenoid = new DoubleSolenoid(CANPort.CAN_S0, PneumaticsModuleType.CTRE_PCM, 0, 1);
        mPressureSensor = new AnalogInput(0);
    }

    public void extend()
    {
        mSolenoid.set(DoubleSolenoid.Value.FORWARD);
    }

    public void retract()
    {
        mSolenoid.set(DoubleSolenoid.Value.REVERSE);
    }

    public double getPressure()
    {
        return mPressureSensor.getVoltage() / 5.0 * 120;
    }

    @Override
    public void close()
    {
        mSolenoid.close();
        mPressureSensor.close();
    }
}
