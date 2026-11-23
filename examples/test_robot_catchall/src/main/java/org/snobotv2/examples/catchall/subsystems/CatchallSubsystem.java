package org.snobotv2.examples.catchall.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import org.wpilib.hardware.accelerometer.ADXL345_I2C;
import org.wpilib.hardware.led.AddressableLED;
import org.wpilib.hardware.accelerometer.AnalogAccelerometer;
import org.wpilib.hardware.rotation.AnalogEncoder;
import org.wpilib.hardware.discrete.AnalogInput;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.rotation.AnalogPotentiometer;
import org.wpilib.hardware.pneumatic.Compressor;
import org.wpilib.hardware.pneumatic.PneumaticsModuleType;
import org.wpilib.hardware.discrete.DigitalInput;
import org.wpilib.hardware.discrete.DigitalOutput;
import org.wpilib.hardware.pneumatic.DoubleSolenoid;
import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.bus.I2C;
import org.wpilib.drivers.motor.PWMSparkMax;
import org.wpilib.drivers.motor.PWMTalonFX;
import org.wpilib.drivers.motor.PWMTalonSRX;
import org.wpilib.drivers.motor.PWMVenom;
import org.wpilib.drivers.motor.PWMVictorSPX;
import org.wpilib.hardware.power.PowerDistribution;
import org.wpilib.hardware.pneumatic.Solenoid;
import org.wpilib.drivers.motor.Spark;
import org.wpilib.command2.SubsystemBase;

@SuppressWarnings({"PMD", "removal"})
public class CatchallSubsystem extends SubsystemBase
{
    protected final ADXL345_I2C mI2cAccelerometer;
    protected final AddressableLED mLeds;
    protected final AnalogAccelerometer mAnalogAccelerometer;
    protected final AnalogEncoder mAnalogEncoder;
    protected final AnalogInput mAnalogInput;
    protected final OnboardIMU mOnboardImu;
    protected final AnalogPotentiometer mAnalogPotentiometer;
    protected final Compressor mCompressor;
    protected final DigitalInput mDigitalInput;
    protected final DigitalOutput mDigitalOutput;
    protected final DoubleSolenoid mDoubleSolenoid;
    protected final Encoder mEncoder;
    protected final I2C mCustomI2c;
    protected final PowerDistribution mPdp;
    protected final PWMSparkMax mPwmSparkMax;
    protected final PWMTalonFX mPwmTalonFx;
    protected final PWMTalonSRX mPwmTalonSRX;
    protected final PWMVenom mVenom;
    protected final PWMVictorSPX mPwmVictorSPX;
    protected final Solenoid mSolenoid;
    protected final Spark mSpark;

    protected final SparkMax mCanSparkMax;
    protected final SparkFlex mCanSparkFlex;

    public CatchallSubsystem()
    {
        mI2cAccelerometer = new ADXL345_I2C(I2C.Port.PORT_1, 2);
        mCustomI2c = new I2C(I2C.Port.PORT_0, 0x20);

        mAnalogInput = new AnalogInput(2);
        mOnboardImu = new OnboardIMU(OnboardIMU.MountOrientation.FLAT);
        mAnalogAccelerometer = new AnalogAccelerometer(3);
        mAnalogEncoder = new AnalogEncoder(new AnalogInput(4));
        mAnalogPotentiometer = new AnalogPotentiometer(5);

        mDigitalInput = new DigitalInput(9);
        mDigitalOutput = new DigitalOutput(10);
        mEncoder =  new Encoder(0, 1);

        mCompressor = new Compressor(CANPort.CAN_S0, 0, PneumaticsModuleType.CTRE_PCM);
        mSolenoid = new Solenoid(CANPort.CAN_S0, 1, PneumaticsModuleType.CTRE_PCM, 0);
        mDoubleSolenoid = new DoubleSolenoid(CANPort.CAN_S0, PneumaticsModuleType.CTRE_PCM, 1, 2);
        mPdp = new PowerDistribution(CANPort.CAN_S0, 1, PowerDistribution.ModuleType.CTRE);

        mLeds = new AddressableLED(0);
        mSpark = new Spark(2);
        mPwmSparkMax = new PWMSparkMax(7);
        mPwmTalonFx = new PWMTalonFX(8);
        mPwmTalonSRX = new PWMTalonSRX(9);
        mVenom = new PWMVenom(12);
        mPwmVictorSPX = new PWMVictorSPX(13);

        mCanSparkMax = new SparkMax(CANPort.CAN_S0, 15, SparkLowLevel.MotorType.kBrushed);
        mCanSparkFlex = new SparkFlex(CANPort.CAN_S0, 17, SparkLowLevel.MotorType.kBrushed);
    }

    public void simulationPeriodic()
    {
        mSpark.setThrottle(-0.9);
        mPwmSparkMax.setThrottle(-0.4);
        mPwmTalonFx.setThrottle(-0.3);
        mPwmTalonSRX.setThrottle(-0.2);
        mVenom.setThrottle(0.2);
        mPwmVictorSPX.setThrottle(0.3);

        mCanSparkMax.setThrottle(0.5);
        mCanSparkFlex.setThrottle(0.5);
    }

}
