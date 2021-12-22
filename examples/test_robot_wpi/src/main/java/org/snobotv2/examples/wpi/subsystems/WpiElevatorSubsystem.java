package org.snobotv2.examples.wpi.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.sim_wrappers.ElevatorSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class WpiElevatorSubsystem extends SubsystemBase implements ElevatorSubsystem
{
    private static final double kGravityCompensationVolts = 0.85;
    private static final double kElevatorKp = 5.25;
    private static final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * ElevatorSimConstants.kElevatorDrumRadius / 4096;

    private final PIDController mController;
    private final Encoder mEncoder;
    private final PWMVictorSPX mMotor;

    private ISimWrapper mSimulator;

    public WpiElevatorSubsystem()
    {
        mController = new PIDController(kElevatorKp, 0, 0);
        mEncoder = new Encoder(BaseConstants.ELEVATOR_ENCODER_A, BaseConstants.ELEVATOR_ENCODER_B);
        mMotor = new PWMVictorSPX(BaseConstants.ELEVATOR_MOTOR_A);
        mEncoder.setDistancePerPulse(kElevatorEncoderDistPerPulse);

        if (RobotBase.isSimulation())
        {
            mSimulator = new ElevatorSimWrapper(ElevatorSimConstants.createSim(), mMotor, mEncoder);
        }
    }

    @Override
    public void close()
    {
        mMotor.close();
        mEncoder.close();
        mController.close();
    }

    @Override
    public void moveManually(double speed)
    {
        mMotor.set(speed);
    }

    @Override
    public void goToPosition(double heightInches)
    {
        double height = Units.inchesToMeters(heightInches);

        double pidOutput = mController.calculate(mEncoder.getDistance(), height);
        double output = pidOutput + kGravityCompensationVolts;
        mMotor.setVoltage(output);
    }

    @Override
    public void goToPositionMotionMagic(double inches)
    {
        throw new UnsupportedOperationException("WPI example does not support motion magic control");
    }

    @Override
    public double getHeightInches()
    {
        return Units.metersToInches(mEncoder.getDistance());
    }

    @Override
    public boolean isAtHeight(double inches, double allowableError)
    {
        return Math.abs(inches - getHeightInches()) < allowableError;
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

    @Override
    public boolean isAtLowerLimit()
    {
        return false;
    }

    @Override
    public boolean isAtUpperLimit()
    {
        return false;
    }
}
