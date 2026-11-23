package org.snobotv2.examples.wpi.subsystems;


import org.wpilib.hardware.rotation.Encoder;
import org.wpilib.drivers.motor.PWMSparkMax;
import org.wpilib.framework.RobotBase;
import org.wpilib.math.controller.PIDController;
import org.wpilib.command2.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;
import org.snobotv2.sim_wrappers.FlywheelSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class WpiShooterSubsystem extends SubsystemBase implements ShooterSubsystem
{
    private static final double KFF = 12.0 / 4700;

    private final Encoder mEncoder;
    private final PWMSparkMax mMotor;
    private final PIDController mPidController;
    private ISimWrapper mSimulator;


    public WpiShooterSubsystem()
    {
        mMotor = new PWMSparkMax(BaseConstants.SHOOTER_MOTOR_A);
        mEncoder = new Encoder(BaseConstants.SHOOTER_ENCODER_A, BaseConstants.SHOOTER_ENCODER_B);
        mPidController = new PIDController(.01, 0, 0);

        if (RobotBase.isSimulation())
        {
            mSimulator = new FlywheelSimWrapper(FlywheelSimConstants.createSim(), mMotor, mEncoder);
        }
    }

    @Override
    public void close()
    {
        mMotor.close();
        mEncoder.close();
    }

    @Override
    public void spinAtRpm(double rpm)
    {
        double pid = mPidController.calculate(mEncoder.getRate(), rpm);
        double voltage = pid + rpm * KFF;

        mMotor.setVoltage(voltage);
    }

    @Override
    public double getRPM()
    {
        return mEncoder.getRate();
    }

    @Override
    public void simulationPeriodic()
    {
        mSimulator.update();
    }

    @Override
    public void stop()
    {
        mMotor.setThrottle(0);
    }
}
