package org.snobotv2.examples.rev.subsystems;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.SparkRelativeEncoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.FlywheelSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class RevShooterSubsystem extends SubsystemBase implements ShooterSubsystem
{
    private final SimableCANSparkMax mLeadMotor; // NOPMD
    private final SimableCANSparkMax mFollower; // NOPMD
    private final RelativeEncoder mEncoder;
    private final SparkPIDController mPidController;
    private ISimWrapper mSimulator;

    public RevShooterSubsystem()
    {
        mLeadMotor = new SimableCANSparkMax(BaseConstants.SHOOTER_MOTOR_A, CANSparkLowLevel.MotorType.kBrushed);
        mFollower = new SimableCANSparkMax(BaseConstants.SHOOTER_MOTOR_B, CANSparkLowLevel.MotorType.kBrushed);

        mFollower.follow(mLeadMotor);

        mEncoder = mLeadMotor.getEncoder(SparkRelativeEncoder.Type.kQuadrature, 8192);
        mPidController = mLeadMotor.getPIDController();

        mPidController.setP(0.0005);
        mPidController.setFF(1.0 / 4700);

        if (RobotBase.isSimulation())
        {
            mSimulator = new FlywheelSimWrapper(FlywheelSimConstants.createSim(),
                    new RevMotorControllerSimWrapper(mLeadMotor),
                    RevEncoderSimWrapper.create(mLeadMotor));
        }
    }

    @Override
    public void close()
    {
        mLeadMotor.close();
        mFollower.close();
    }

    @Override
    public void spinAtRpm(double rpm)
    {
        mPidController.setReference(rpm, ControlType.kVelocity);
    }

    @Override
    public double getRPM()
    {
        return mEncoder.getVelocity();
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
