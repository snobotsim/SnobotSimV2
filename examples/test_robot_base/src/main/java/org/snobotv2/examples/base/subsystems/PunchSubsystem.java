package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PunchSubsystem extends SubsystemBase implements AutoCloseable
{
    private final DoubleSolenoid mSolenoid;
    private final AnalogInput mPressureSensor;

    public PunchSubsystem()
    {
        mSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
        mPressureSensor = new AnalogInput(0);
    }

    public void extend()
    {
        mSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void retract()
    {
        mSolenoid.set(DoubleSolenoid.Value.kReverse);
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
