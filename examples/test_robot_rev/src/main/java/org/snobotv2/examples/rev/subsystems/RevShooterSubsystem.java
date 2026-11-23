package org.snobotv2.examples.rev.subsystems;

import org.wpilib.hardware.bus.CANPort;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.wpilib.framework.RobotBase;
import org.wpilib.command2.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ShooterSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.FlywheelSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class RevShooterSubsystem extends SubsystemBase implements ShooterSubsystem
{
    private final SparkMax mLeadMotor; // NOPMD
    private final SparkMax mFollower; // NOPMD
    private final RelativeEncoder mEncoder;
    private final SparkClosedLoopController mPidController;
    private ISimWrapper mSimulator;

    public RevShooterSubsystem()
    {
        SparkMaxConfig commonConfig = new SparkMaxConfig();
        commonConfig.closedLoop.p(0.0005);
        commonConfig.closedLoop.feedForward.kV(1.0 / 4700);

        ResetMode resetMode = ResetMode.kResetSafeParameters;
        PersistMode persistMode = PersistMode.kPersistParameters;

        mLeadMotor = new SparkMax(CANPort.CAN_S0, BaseConstants.SHOOTER_MOTOR_A, MotorType.kBrushless);
        mLeadMotor.configure(new SparkMaxConfig()
                .apply(commonConfig),
                resetMode, persistMode);

        mFollower = new SparkMax(CANPort.CAN_S0, BaseConstants.SHOOTER_MOTOR_B, MotorType.kBrushless);
        mFollower.configure(new SparkMaxConfig()
                        .apply(commonConfig)
                        .follow(mLeadMotor),
                resetMode, persistMode);

        mEncoder = mLeadMotor.getEncoder();
        mPidController = mLeadMotor.getClosedLoopController();

        if (RobotBase.isSimulation())
        {
            mSimulator = new FlywheelSimWrapper(FlywheelSimConstants.createSim(),
                    new RevMotorControllerSimWrapper(mLeadMotor, FlywheelSimConstants.kGearbox),
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
        mPidController.setSetpoint(rpm, ControlType.kVelocity);
    }

    @Override
    public double getRPM()
    {
        return mEncoder.getVelocity().get();
    }

    @Override
    public void simulationPeriodic()
    {
        mSimulator.update();
    }

    @Override
    public void stop()
    {
        mLeadMotor.setThrottle(0);
    }
}
