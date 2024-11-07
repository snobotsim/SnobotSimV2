package com.revrobotics.spark;

import com.revrobotics.sim.SparkFlexSim;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkLowLevel.SparkModel;
import com.revrobotics.spark.config.ClosedLoopConfig.ClosedLoopSlot;
import com.revrobotics.spark.config.ClosedLoopConfigAccessor;
import com.revrobotics.spark.config.SparkBaseConfigAccessor;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.SimInt;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.CouplingBetweenObjects"})
public class SparkSimHelper
{
    private static final Logger LOGGER = Logger.getLogger(SparkSimHelper.class.getName());
    private static final Map<Integer, ControlType> CONTROL_TYPE_LOOKUP;
    private static final Map<Integer, ArbFFUnits> ARB_FF_UNITS_LOOKUP;

    private final Map<ClosedLoopSlot, PIDFConstants> mPidConstants;
    private final SparkBase mSpark;
    private final SparkSim mSim;
    private final SparkBaseConfigAccessor mConfigAccessor;

    private final SimInt mControlMode;
    private final SimDouble mArbFF;
    private final SimInt mArbFFUnits;

    static
    {
        CONTROL_TYPE_LOOKUP = new HashMap<>();
        for (ControlType t : ControlType.values())
        {
            CONTROL_TYPE_LOOKUP.put(t.value, t);
        }

        ARB_FF_UNITS_LOOKUP = new HashMap<>();
        for (ArbFFUnits t : ArbFFUnits.values())
        {
            ARB_FF_UNITS_LOOKUP.put(t.value, t);
        }
    }

    public SparkSimHelper(SparkBase spark, DCMotor motor)
    {
        mSpark = spark;
        if (mSpark instanceof SparkMax)
        {
            mSim = new SparkMaxSim((SparkMax) mSpark, motor);
            mConfigAccessor = ((SparkMax) mSpark).configAccessor;
        }
        else if (mSpark instanceof SparkFlex)
        {
            mSim = new SparkFlexSim((SparkFlex) mSpark, motor);
            mConfigAccessor = ((SparkFlex) mSpark).configAccessor;
        }
        else
        {
            throw new IllegalArgumentException("The provided motor controller is not simmable!");
        }

        mPidConstants = new EnumMap<>(ClosedLoopSlot.class);

        String deviceType = "UNKNOWN";
        if (spark.getSparkModel() == SparkModel.SparkFlex)
        {
            deviceType = "SPARK Flex";
        } else if (spark.getSparkModel() == SparkModel.SparkMax)
        {
            deviceType = "SPARK MAX";
        }

        String deviceName = deviceType + " [" + spark.getDeviceId() + "]";
        SimDeviceSim sparkSim = new SimDeviceSim(deviceName);

        mControlMode = sparkSim.getInt("Control Mode");
        mArbFF = sparkSim.getDouble("Arbitrary Feedforward");
        mArbFFUnits = sparkSim.getInt("ArbFF Units");
    }


    @SuppressWarnings({"removal", "PMD.CyclomaticComplexity"})
    public void updateSim()
    {
        int controlTypeInt = mControlMode.get();
        if (controlTypeInt == -1)
        {
            return;
        }

        ControlType controlType = CONTROL_TYPE_LOOKUP.get(controlTypeInt);

        double voltagePercentage = 0;
        switch (controlType)
        {
        case kDutyCycle:
            voltagePercentage = mSim.getSetpoint();
            break;
        case kVelocity:
            voltagePercentage = calculateSpeedControl();
            break;
        case kPosition:
            voltagePercentage = calculatePositionControl();
            break;
        case kMAXMotionPositionControl:
            voltagePercentage = calculateSmartMotionOutput();
            break;
        case kVoltage:
            voltagePercentage = mSim.getSetpoint() / RobotController.getBatteryVoltage();
            break;
        case kMAXMotionVelocityControl:
        case kCurrent:
        case kSmartVelocity:
        case kSmartMotion:
        default:
            throw new IllegalArgumentException("Unsupported control type " + controlType);
        }

        mSim.setAppliedOutput(voltagePercentage);
    }

    protected void log(Level level, String message)
    {
        LOGGER.log(level, "REV Sim [" + mSpark.getDeviceId() + "] - " + message);
    }

    public void setCurrent(double current)
    {
        mSim.setMotorCurrent(current);
    }

    @SuppressWarnings({"PMD.ShortVariable"})
    private static class PIDFConstants
    {
        protected final PIDController mBasicPidController;
        protected final ProfiledPIDController mProfiledPidController;
        protected Constraints mConstraints;
        private double mF;
        private double mMaxVelocity;
        private double mMaxAcceleration;

        public PIDFConstants(ClosedLoopSlot slot)
        {
            mConstraints = new Constraints(0, 0);
            mBasicPidController = new PIDController(0, 0, 0);
            mProfiledPidController = new ProfiledPIDController(0, 0, 0, mConstraints);
            SendableRegistry.setName(mBasicPidController, "RevSim[" + slot.value + "] Basic PID");
            SendableRegistry.setName(mProfiledPidController, "RevSim[" + slot.value + "] Profiled PID");
            mF = 0;
        }

        @SuppressWarnings("PMD.ShortVariable")
        private void refreshValues(ClosedLoopSlot slotId, ClosedLoopConfigAccessor accessor)
        {
            double kp = accessor.getP(slotId);
            double ki = accessor.getI(slotId);
            double kd = accessor.getD(slotId);
            mF = accessor.getFF(slotId);

            mBasicPidController.setP(kp);
            mProfiledPidController.setP(kp);

            mBasicPidController.setI(ki);
            mProfiledPidController.setI(ki);

            mBasicPidController.setD(kd);
            mProfiledPidController.setD(kd);

            mMaxVelocity = accessor.maxMotion.getMaxVelocity(slotId);
            mMaxAcceleration = accessor.maxMotion.getMaxAcceleration(slotId);
            mConstraints = new Constraints(mMaxVelocity, mMaxAcceleration);
            mProfiledPidController.setConstraints(mConstraints);
        }

        public void reset()
        {
            mBasicPidController.reset();
            mProfiledPidController.reset(0);
        }
    }

    private double getArbPercentOutput()
    {
        int arbFfUnitInt = mArbFFUnits.get();
        ArbFFUnits arbFfUnit = ARB_FF_UNITS_LOOKUP.get(arbFfUnitInt);
        double arbFFPercentage;
        if (arbFfUnit == ArbFFUnits.kPercentOut)
        {
            arbFFPercentage = mArbFF.get();
        }
        else if (arbFfUnit == ArbFFUnits.kVoltage)
        {
            arbFFPercentage = mArbFF.get() / RobotController.getBatteryVoltage();
        }
        else
        {
            arbFFPercentage = 0;
            log(Level.SEVERE, "Unknown Arb FF unit: " + arbFfUnit);
        }

        return arbFFPercentage;
    }

    private double constrainOutput(double inPercent)
    {
        return Math.min(1, Math.max(-1, inPercent));
    }


    private PIDFConstants getActivePid()
    {
        ClosedLoopSlot activeSlot = mSim.getClosedLoopSlot();
        if (!mPidConstants.containsKey(activeSlot))
        {
            mPidConstants.put(activeSlot, new PIDFConstants(activeSlot));
        }

        PIDFConstants constants = mPidConstants.get(activeSlot);
        constants.refreshValues(activeSlot, mConfigAccessor.closedLoop); // FJDLKS
        return constants;
    }

    private double calculatePositionControl()
    {
        double goal = mSim.getSetpoint();
        PIDFConstants activePid = getActivePid();
        double output = activePid.mBasicPidController.calculate(getPosition(), goal);
        output += getArbPercentOutput();
        output = constrainOutput(output);
        log(Level.FINE, "Updating position control.... " + getPosition() + " vs " + goal + " -> " + output);
        return output;
    }

    @SuppressWarnings("PMD.ShortVariable")
    private double calculateSpeedControl()
    {
        double goal = mSim.getSetpoint();
        PIDFConstants activePid = getActivePid();
        double ff = goal * activePid.mF;
        double pid = activePid.mBasicPidController.calculate(getVelocity(), goal);
        double output = ff + pid + getArbPercentOutput();
        output = constrainOutput(output);
        log(Level.FINE, "Updating Speed control.... " + getVelocity() + " vs " + goal + " -> " + output + "(" + ff + " + " + pid + ")");

        return output;
    }

    private double calculateSmartMotionOutput()
    {
        double goal = mSim.getSetpoint();

        PIDFConstants activePid = getActivePid();
        if (activePid.mMaxAcceleration == 0)
        {
            log(Level.WARNING, "Max acceleration has not been set");
        }
        if (activePid.mMaxVelocity == 0)
        {
            log(Level.WARNING, "Max velocity has not been set");
        }

        double position = getPosition();

        double pid = activePid.mProfiledPidController.calculate(position, goal);
        State setpoint = activePid.mProfiledPidController.getSetpoint();

        double arbFfPercentage = getArbPercentOutput();

        double ff = activePid.mF * setpoint.velocity; // NOPMD(ShortVariable)
        double output = constrainOutput(ff + pid + arbFfPercentage);
        log(Level.FINE, "Updating MM control.... "
                + "Going to " + goal + "... "
                + "Position " + position + " vs " + setpoint.position + ", "
                + "Velocity " + getVelocity() + " vs " + setpoint.velocity + ", "
                + " -> output " + output + "(" + ff + " + " + pid + " + " + arbFfPercentage + ")"
                + " constraints: Vel: " + activePid.mConstraints.maxVelocity + ", Accel: " + activePid.mConstraints.maxAcceleration);

        return output;
    }

    private double getVelocity()
    {
        return mSim.getVelocity();
    }

    private double getPosition()
    {
        return mSim.getPosition();
    }
}
