package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedSubsystem extends SubsystemBase implements AutoCloseable
{
    private final Relay mRelay;
    private final AnalogOutput mVariableBrightnessLeds; 
    private final AddressableLED mAddressableLeds;
    private final AddressableLEDBuffer mLedBuffer;
    private int mRainbowFirstPixelHue;

    public LedSubsystem()
    {
        mRelay = new Relay(0);
        mVariableBrightnessLeds = new AnalogOutput(0);

        mAddressableLeds = new AddressableLED(0);
        mLedBuffer = new AddressableLEDBuffer(60);
        mAddressableLeds.setLength(mLedBuffer.getLength());
        mAddressableLeds.setData(mLedBuffer);
        mAddressableLeds.start();
    }

    @Override
    public void close()
    {
        mRelay.close();
        mVariableBrightnessLeds.close();
        mAddressableLeds.close();
    }

    public void turnOnRedAndBlue()
    {
        mRelay.set(Relay.Value.kOn);
    }

    public void turnOnRed()
    {
        mRelay.set(Relay.Value.kReverse);
    }

    public void turnOnBlue()
    {
        mRelay.set(Relay.Value.kForward);
    }

    public void turnAllOff()
    {
        mRelay.set(Relay.Value.kOff);
    }

    @Override
    public void periodic()
    {
        rainbow();
        mAddressableLeds.setData(mLedBuffer);
        mVariableBrightnessLeds.setVoltage(mRainbowFirstPixelHue / 180.0 * 4.5);
    }

    private void rainbow()
    {
        // For every pixel
        for (var i = 0; i < mLedBuffer.getLength(); i++)
        {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var hue = (mRainbowFirstPixelHue + (i * 180 / mLedBuffer.getLength())) % 180;
            // Set the value
            mLedBuffer.setHSV(i, hue, 255, 128);
        }
        // Increase by to make the rainbow "move"
        mRainbowFirstPixelHue += 3;
        // Check bounds
        mRainbowFirstPixelHue %= 180;
    }
}
