package org.snobotv2.examples.ctre.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;
import org.snobotv2.module_wrappers.ctre.CtreEncoderSimWrapper;
import org.snobotv2.module_wrappers.ctre.CtreMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.FlywheelSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class CtreShooterSubsystem extends SubsystemBase implements ShooterSubsystem
{
    private static final double CTRE_VEL_TICKS_PER_RPM = 4096.0 / 100;

    private final WPI_TalonSRX mLeadMotor;
    private final WPI_TalonSRX mFollwerMotor; // NOPMD
    private ISimWrapper mSimulator;


    public CtreShooterSubsystem()
    {
        mLeadMotor = new WPI_TalonSRX(BaseConstants.SHOOTER_MOTOR_A);
        mLeadMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 5);
        mLeadMotor.config_kP(0, .0004);
        mLeadMotor.config_kF(0, .0032);

        mFollwerMotor = new WPI_TalonSRX(BaseConstants.SHOOTER_MOTOR_B);
        mFollwerMotor.follow(mLeadMotor);

        if (RobotBase.isSimulation())
        {
            mSimulator = new FlywheelSimWrapper(
                    FlywheelSimConstants.createSim(),
                    new CtreMotorControllerSimWrapper(mLeadMotor),
                    new CtreEncoderSimWrapper(mLeadMotor));
        }
    }

    @Override
    public void close()
    {
        mLeadMotor.free();
        mFollwerMotor.free();
    }

    @Override
    public void spinAtRpm(double rpm)
    {
        double targetVelocityUnitsPer100ms = rpm * CTRE_VEL_TICKS_PER_RPM;
        mLeadMotor.set(ControlMode.Velocity, targetVelocityUnitsPer100ms);
    }

    @Override
    public double getRPM()
    {
        return mLeadMotor.getSelectedSensorVelocity() / CTRE_VEL_TICKS_PER_RPM;
    }

    @Override
    public void simulationPeriodic()
    {
        mSimulator.update();
    }


    @Override
    public void stop()
    {
        mLeadMotor.set(0);
    }
}
