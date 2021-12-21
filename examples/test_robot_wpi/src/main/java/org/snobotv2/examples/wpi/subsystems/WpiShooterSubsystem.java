package org.snobotv2.examples.wpi.subsystems;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
        mPidController.close();
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
        mMotor.set(0);
    }
}
