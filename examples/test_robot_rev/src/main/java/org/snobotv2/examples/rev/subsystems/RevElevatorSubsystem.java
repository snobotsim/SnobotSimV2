package org.snobotv2.examples.rev.subsystems;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SimableCANSparkMax;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.ElevatorSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class RevElevatorSubsystem extends SubsystemBase implements ElevatorSubsystem
{
    private static final double GRAVITY_COMPENSATION_VOLTS = 0.85;
    private static final double TICKS_PER_METER = 4096;
    private static final int POSITION_SLOT = 0;
    private static final int SMART_MOTION_SLOT = 1;

    private final SimableCANSparkMax mLeadMotor; // NOPMD
    private final SimableCANSparkMax mFollowerMotor; // NOPMD
    private final RelativeEncoder mEncoder;
    private final SparkPIDController mPidController;
    private ISimWrapper mElevatorSim;

    public RevElevatorSubsystem()
    {
        mLeadMotor = new SimableCANSparkMax(BaseConstants.ELEVATOR_MOTOR_A, CANSparkLowLevel.MotorType.kBrushless);
        mFollowerMotor = new SimableCANSparkMax(BaseConstants.ELEVATOR_MOTOR_B, CANSparkLowLevel.MotorType.kBrushless);
        mFollowerMotor.follow(mLeadMotor);
        mEncoder = mLeadMotor.getEncoder();
        mPidController = mLeadMotor.getPIDController();

        mEncoder.setPositionConversionFactor(TICKS_PER_METER);

        mPidController.setP(0.16);

        if (RobotBase.isSimulation())
        {
            mElevatorSim = new ElevatorSimWrapper(ElevatorSimConstants.createSim(),
                    new RevMotorControllerSimWrapper(mLeadMotor),
                    RevEncoderSimWrapper.create(mLeadMotor));
        }
    }

    @Override
    public void close()
    {
        mLeadMotor.close();
        mFollowerMotor.close();
    }

    @Override
    public void moveManually(double speed)
    {
        mLeadMotor.set(speed);
    }

    @Override
    public void goToPosition(double inches)
    {
        double meters = Units.inchesToMeters(inches);
        mPidController.setReference(meters, ControlType.kPosition, POSITION_SLOT, GRAVITY_COMPENSATION_VOLTS, SparkPIDController.ArbFFUnits.kVoltage);
    }

    @Override
    public void goToPositionMotionMagic(double inches)
    {
        double meters = Units.inchesToMeters(inches);
        mPidController.setReference(meters, ControlType.kSmartMotion, SMART_MOTION_SLOT);
    }

    @Override
    public boolean isAtHeight(double inches, double allowableError)
    {
        return Math.abs(inches - getHeightInches()) < allowableError;
    }

    @Override
    public double getHeightInches()
    {
        return Units.metersToInches(mEncoder.getPosition());
    }

    @Override
    public void simulationPeriodic()
    {
        mElevatorSim.update();
    }

    @Override
    public void stop()
    {
        mLeadMotor.set(0);
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
