package org.snobotv2.examples.catchall.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
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
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.DMC60;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.motorcontrol.Jaguar;
import edu.wpi.first.wpilibj.motorcontrol.NidecBrushless;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVenom;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.motorcontrol.SD540;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@SuppressWarnings({"PMD", "removal"})
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
    protected final PowerDistribution mPdp;
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
    protected final SparkMax mCanSparkMax;
    protected final SparkFlex mCanSparkFlex;
    protected final WPI_TalonSRX mTalonSrx;
    protected final WPI_VictorSPX mVictorSpx;

    public CatchallSubsystem()
    {
        mI2cAccelerometer = new ADXL345_I2C(I2C.Port.kMXP, ADXL345_I2C.Range.k2G);
        mSpiAccelerometer = new ADXL345_SPI(SPI.Port.kMXP, ADXL345_SPI.Range.k2G);
        mM362Accelerometer = new ADXL362(SPI.Port.kMXP, ADXL362.Range.k2G);
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

        mCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
        mSolenoid = new Solenoid(1, PneumaticsModuleType.CTREPCM, 0);
        mDoubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 2);
        mPdp = new PowerDistribution(1, PowerDistribution.ModuleType.kCTRE);
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

        mCanSparkMax = new SparkMax(15, SparkBase.MotorType.kBrushed);
        mCanSparkFlex = new SparkFlex(17, SparkLowLevel.MotorType.kBrushed);
        mTalonSrx = new WPI_TalonSRX(16);
        mVictorSpx = new WPI_VictorSPX(18);

        mNavx = new AHRS(NavXComType.kMXP_SPI);
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
        mCanSparkFlex.set(0.5);
        mTalonSrx.set(0.6);
        mVictorSpx.set(0.8);
    }

}
