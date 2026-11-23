package org.snobotv2.examples.base.subsystems;

import org.wpilib.command2.SubsystemBase;
import org.wpilib.hardware.led.AddressableLED;
import org.wpilib.hardware.led.AddressableLEDBuffer;

public class LedSubsystem extends SubsystemBase implements AutoCloseable
{
    private enum Mode
    {
        RAINBOW(0, 0, 0),
        RED_AND_BLUE(255, 0, 255),
        RED(255, 0, 0),
        BLUE(0, 0, 255),
        OFF(0, 0, 0);

        private final int mRed;
        private final int mGreen;
        private final int mBlue;

        Mode(int red, int green, int blue)
        {
            mRed = red;
            mGreen = green;
            mBlue = blue;
        }
    }

    private final AddressableLED mAddressableLeds;
    private final AddressableLEDBuffer mLedBuffer;
    private int mRainbowFirstPixelHue;
    private Mode mMode;

    public LedSubsystem()
    {
        mAddressableLeds = new AddressableLED(0);
        mLedBuffer = new AddressableLEDBuffer(60);
        mAddressableLeds.setLength(mLedBuffer.getLength());
        mAddressableLeds.setData(mLedBuffer);
        mMode = Mode.RAINBOW;
    }

    @Override
    public void close()
    {
        mAddressableLeds.close();
    }

    public void turnOnRedAndBlue()
    {
        mMode = Mode.RED_AND_BLUE;
    }

    public void turnOnRed()
    {
        mMode = Mode.RED;
    }

    public void turnOnBlue()
    {
        mMode = Mode.BLUE;
    }

    public void turnAllOff()
    {
        mMode = Mode.OFF;
    }

    @Override
    public void periodic()
    {
        if (mMode == Mode.RAINBOW)
        {
            rainbow();
        }
        else
        {
            fill(mMode.mRed, mMode.mGreen, mMode.mBlue);
        }
        mAddressableLeds.setData(mLedBuffer);
    }

    private void fill(int red, int green, int blue)
    {
        for (int i = 0; i < mLedBuffer.getLength(); i++)
        {
            mLedBuffer.setRGB(i, red, green, blue);
        }
    }

    private void rainbow()
    {
        // For every pixel
        for (int i = 0; i < mLedBuffer.getLength(); i++)
        {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final int hue = (mRainbowFirstPixelHue + (i * 180 / mLedBuffer.getLength())) % 180;
            // Set the value
            mLedBuffer.setHSV(i, hue, 255, 128);
        }
        // Increase by to make the rainbow "move"
        mRainbowFirstPixelHue += 3;
        // Check bounds
        mRainbowFirstPixelHue %= 180;
    }
}
