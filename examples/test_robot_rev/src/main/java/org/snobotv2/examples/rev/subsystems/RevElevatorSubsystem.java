package org.snobotv2.examples.rev.subsystems;

import org.wpilib.hardware.bus.CANPort;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkLowLevel.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.wpilib.math.util.Units;
import org.wpilib.framework.RobotBase;
import org.wpilib.command2.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.ElevatorSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class RevElevatorSubsystem extends SubsystemBase implements ElevatorSubsystem
{
    private static final double GRAVITY_COMPENSATION_VOLTS = 0.85;
    // REVLib 2027 removed on-device conversion factors, so the encoder reports rotations.
    // This converts native units to meters.
    private static final double METERS_PER_ROTATION = 4096;
    private static final ClosedLoopSlot POSITION_SLOT = ClosedLoopSlot.kSlot0;
    private static final ClosedLoopSlot SMART_MOTION_SLOT = ClosedLoopSlot.kSlot1;

    private final SparkMax mLeadMotor; // NOPMD
    private final SparkMax mFollowerMotor; // NOPMD
    private final RelativeEncoder mEncoder;
    private final SparkClosedLoopController mPidController;
    private ISimWrapper mElevatorSim;

    public RevElevatorSubsystem()
    {
        SparkMaxConfig commonConfig = new SparkMaxConfig();
        // The gains were tuned in meters, so scale them into native units
        commonConfig.closedLoop.p(0.16 * METERS_PER_ROTATION);

        ResetMode resetMode = ResetMode.kResetSafeParameters;
        PersistMode persistMode = PersistMode.kPersistParameters;

        mLeadMotor = new SparkMax(CANPort.CAN_S0, BaseConstants.ELEVATOR_MOTOR_A, MotorType.kBrushless);
        mLeadMotor.configure(
                new SparkMaxConfig()
                        .apply(commonConfig),
                resetMode, persistMode);

        mFollowerMotor = new SparkMax(CANPort.CAN_S0, BaseConstants.ELEVATOR_MOTOR_B, MotorType.kBrushless);
        mFollowerMotor.configure(
                new SparkMaxConfig()
                        .apply(commonConfig)
                        .follow(mLeadMotor),
                resetMode, persistMode);

        mEncoder = mLeadMotor.getEncoder();
        mPidController = mLeadMotor.getClosedLoopController();


        if (RobotBase.isSimulation())
        {
            mElevatorSim = new ElevatorSimWrapper(ElevatorSimConstants.createSim(),
                    new RevMotorControllerSimWrapper(mLeadMotor, ElevatorSimConstants.kElevatorGearbox),
                    RevEncoderSimWrapper.create(mLeadMotor, METERS_PER_ROTATION, METERS_PER_ROTATION / 60.0));
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
        mLeadMotor.setThrottle(speed);
    }

    @Override
    public void goToPosition(double inches)
    {
        double meters = Units.inchesToMeters(inches);
        mPidController.setSetpoint(meters / METERS_PER_ROTATION, ControlType.kPosition, POSITION_SLOT, GRAVITY_COMPENSATION_VOLTS, SparkClosedLoopController.ArbFFUnits.kVoltage);
    }

    @Override
    public void goToPositionMotionMagic(double inches)
    {
        double meters = Units.inchesToMeters(inches);
        mPidController.setSetpoint(meters / METERS_PER_ROTATION, ControlType.kMAXMotionPositionControl, SMART_MOTION_SLOT);
    }

    @Override
    public boolean isAtHeight(double inches, double allowableError)
    {
        return Math.abs(inches - getHeightInches()) < allowableError;
    }

    @Override
    public double getHeightInches()
    {
        return Units.metersToInches(mEncoder.getPosition().get() * METERS_PER_ROTATION);
    }

    @Override
    public void simulationPeriodic()
    {
        mElevatorSim.update();
    }

    @Override
    public void stop()
    {
        mLeadMotor.setThrottle(0);
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
