package org.snobotv2.examples.catchall.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DMC60;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.NidecBrushless;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.PWMTalonFX;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.PWMVenom;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SD540;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@SuppressWarnings("PMD")
public class CatchallSubsystem extends SubsystemBase
{
    protected final ADXL345_I2C mI2cAccelerometer;
    protected final ADXL345_SPI mSpiAccelerometer;
    protected final ADXL362 mM362Accelerometer;
    protected final ADXRS450_Gyro mAdxGyro;
    protected final AddressableLED mLeds;
    protected final AnalogAccelerometer mAnalogAccelerometer;
    protected final AnalogEncoder mAnalogEncoder;
    protected final AnalogGyro mAnalogGyro;
    protected final AnalogInput mAnalogInput;
    protected final AnalogOutput mAnalogOutput;
    protected final AnalogPotentiometer mAnalogPotentiometer;
    protected final Compressor mCompressor;
    protected final DigitalInput mDigitalInput;
    protected final DigitalOutput mDigitalOutput;
    protected final DMC60 mDmc60;
    protected final DoubleSolenoid mDoubleSolenoid;
    protected final Encoder mEncoder;
    protected final I2C mCustomI2c;
    protected final Jaguar mJaguar;
    protected final NidecBrushless mNidec;
    protected final PowerDistributionPanel mPdp;
    protected final PWMSparkMax mPwmSparkMax;
    protected final PWMTalonFX mPwmTalonFx;
    protected final PWMTalonSRX mPwmTalonSRX;
    protected final PWMVenom mVenom;
    protected final PWMVictorSPX mPwmVictorSPX;
    protected final Relay mRelay;
    protected final SD540 mSd540;
    protected final Servo mServo;
    protected final Solenoid mSolenoid;
    protected final Spark mSpark;
    protected final SPI mCustomSpi;
    protected final Talon mTalon;
    protected final Ultrasonic mUltrasonic;
    protected final Victor mVictor;
    protected final VictorSP mVictorSP;

    protected final AHRS mNavx;
    protected final CANSparkMax mCanSparkMax;
    protected final WPI_TalonSRX mTalonSrx;
    protected final WPI_TalonFX mTalonFx;
    protected final WPI_VictorSPX mVictorSpx;

    public CatchallSubsystem()
    {
        mI2cAccelerometer = new ADXL345_I2C(I2C.Port.kMXP, Accelerometer.Range.k2G);
        mSpiAccelerometer = new ADXL345_SPI(SPI.Port.kMXP, Accelerometer.Range.k2G);
        mM362Accelerometer = new ADXL362(SPI.Port.kMXP, Accelerometer.Range.k2G);
        mAdxGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS3);
        mCustomSpi = new SPI(SPI.Port.kOnboardCS3);
        mCustomI2c = new I2C(I2C.Port.kOnboard, 0x20);

        mAnalogGyro = new AnalogGyro(1);
        mAnalogInput = new AnalogInput(2);
        mAnalogAccelerometer = new AnalogAccelerometer(3);
        mAnalogEncoder = new AnalogEncoder(new AnalogInput(4));
        mAnalogPotentiometer = new AnalogPotentiometer(5);
        mAnalogOutput = new AnalogOutput(1);

        mDigitalInput = new DigitalInput(9);
        mDigitalOutput = new DigitalOutput(10);
        mEncoder =  new Encoder(0, 1);
        mUltrasonic = new Ultrasonic(3, 4);

        mCompressor = new Compressor(0);
        mSolenoid = new Solenoid(1, 0);
        mDoubleSolenoid = new DoubleSolenoid(1, 2);
        mPdp = new PowerDistributionPanel(1);
        mRelay = new Relay(3);

        mLeds = new AddressableLED(0);
        mServo = new Servo(1);
        mSpark = new Spark(2);
        mTalon = new Talon(3);
        mVictor = new Victor(4);
        mVictorSP = new VictorSP(5);
        mJaguar = new Jaguar(6);
        mPwmSparkMax = new PWMSparkMax(7);
        mPwmTalonFx = new PWMTalonFX(8);
        mPwmTalonSRX = new PWMTalonSRX(9);
        mNidec = new NidecBrushless(15, 8);
        mDmc60 = new DMC60(11);
        mVenom = new PWMVenom(12);
        mPwmVictorSPX = new PWMVictorSPX(13);
        mSd540 = new SD540(14);

        mCanSparkMax = new CANSparkMax(15, CANSparkMaxLowLevel.MotorType.kBrushed);
        mTalonSrx = new WPI_TalonSRX(16);
        mTalonFx = new WPI_TalonFX(17);
        mVictorSpx = new WPI_VictorSPX(18);

        mNavx = new AHRS();
    }

    public void simulationPeriodic()
    {
        mServo.set(.25);
        mSpark.set(-0.9);
        mTalon.set(-0.8);
        mVictor.set(-0.7);
        mVictorSP.set(-0.6);
        mJaguar.set(-0.5);
        mPwmSparkMax.set(-0.4);
        mPwmTalonFx.set(-0.3);
        mPwmTalonSRX.set(-0.2);
        mNidec.set(-0.1);
        mDmc60.set(0.1);
        mVenom.set(0.2);
        mPwmVictorSPX.set(0.3);
        mSd540.set(0.4);

        mCanSparkMax.set(0.5);
        mTalonSrx.set(0.6);
        mTalonFx.set(0.7);
        mVictorSpx.set(0.8);
    }

}
