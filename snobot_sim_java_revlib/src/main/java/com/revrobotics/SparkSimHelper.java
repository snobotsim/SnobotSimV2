package com.revrobotics;

import com.revrobotics.jni.CANSparkMaxJNI;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods"})
public class SparkSimHelper
{
    private static final Logger LOGGER = Logger.getLogger(SparkSimHelper.class.getName());
    private static final int NUM_PID_SLOTS = 4;

    private CANSparkBase.ControlType mControlType;
    private double mControlGoal;
    private double mArbFFPercentage;

    private SimablePidController mLatchedPidController;
    private final SimDouble mAppliedOutputSim;
    private final List<SimableCANSparkMax> mSparkMaxFollowers;
    private final List<SimableCANSparkFlex> mSparkFlexFollowers;


    private final PIDFConstants[] mPidConstants;
    private int mActivePidSlot;

    // Encoder helpers that are private in the base class
    private RelativeEncoder mLatchedEncoder;

    private final long mSparkMaxHandle;
    private final CANSparkBase mSparkMax;

    @SuppressWarnings({"PMD.ShortVariable"})
    protected class PIDFConstants
    {
        protected final PIDController mBasicPidController;
        protected final ProfiledPIDController mProfiledPidController;
        protected TrapezoidProfile.Constraints mConstraints;
        private double mF;
        private double mMaxVelocity;
        private double mMaxAcceleration;

        public PIDFConstants(int slot)
        {
            mConstraints = new TrapezoidProfile.Constraints(0, 0);
            mBasicPidController = new PIDController(0, 0, 0);
            mProfiledPidController = new ProfiledPIDController(0, 0, 0, mConstraints);
            SendableRegistry.setName(mBasicPidController, "RevSim[" + slot + "] Basic PID");
            SendableRegistry.setName(mProfiledPidController, "RevSim[" + slot + "] Profiled PID");
            mF = 0;
        }

        @SuppressWarnings("PMD.ShortVariable")
        private void refreshValues(int slotId, SimablePidController simablePid)
        {
            double kp = CANSparkMaxJNI.c_SparkMax_GetP(mSparkMaxHandle, slotId);
            double ki = CANSparkMaxJNI.c_SparkMax_GetI(mSparkMaxHandle, slotId);
            double kd = CANSparkMaxJNI.c_SparkMax_GetD(mSparkMaxHandle, slotId);
            mF = CANSparkMaxJNI.c_SparkMax_GetFF(mSparkMaxHandle, slotId);

            mBasicPidController.setP(kp);
            mProfiledPidController.setP(kp);

            mBasicPidController.setI(ki);
            mProfiledPidController.setI(ki);

            mBasicPidController.setD(kd);
            mProfiledPidController.setD(kd);

            if (simablePid != null)
            {
                mMaxVelocity = simablePid.mMaxVel[slotId];
                mMaxAcceleration = simablePid.mMaxAccel[slotId];
                mConstraints = new TrapezoidProfile.Constraints(mMaxVelocity, mMaxAcceleration);
                mProfiledPidController.setConstraints(mConstraints);
            }
        }

        public void reset()
        {
            mBasicPidController.reset();
            mProfiledPidController.reset(0);
        }
    }

    @SuppressWarnings("PMD.NullAssignment")
    public SparkSimHelper(SimDeviceSim deviceSim, CANSparkBase sparkMax, long sparkMaxHandle)
    {
        mSparkMaxHandle = sparkMaxHandle;
        mSparkMax = sparkMax;

        mAppliedOutputSim = deviceSim.getDouble("Applied Output");
        mSparkMaxFollowers = new ArrayList<>();
        mSparkFlexFollowers = new ArrayList<>();
        mControlType = CANSparkBase.ControlType.kDutyCycle;

        mPidConstants = new PIDFConstants[NUM_PID_SLOTS];
        for (int i = 0; i < NUM_PID_SLOTS; ++i)
        {
            mPidConstants[i] = null;
        }
    }

    public REVLibError follow(final CANSparkBase leader)
    {
        if (leader instanceof SimableCANSparkMax)
        {
            ((SimableCANSparkMax) leader).mSimHelper.addSimFollower((SimableCANSparkMax) mSparkMax);
        }
        else if (leader instanceof SimableCANSparkFlex)
        {
            ((SimableCANSparkFlex) leader).mSimHelper.addSimFollower((SimableCANSparkFlex) mSparkMax);
        }
        else
        {
            log(Level.WARNING, "Cannot follow a non-simable controller");
        }

        return REVLibError.kOk;
    }

    public SparkPIDController getPIDController()
    {
        mLatchedPidController = new SimablePidController(mSparkMax);
        return mLatchedPidController;
    }

    public REVLibError setpointCommand(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedforward, int arbFFUnits)
    {
        mActivePidSlot = pidSlot;
        mArbFFPercentage = getArbPercentOutput(arbFeedforward, arbFFUnits);
        mControlGoal = value;

        if (ctrl == CANSparkBase.ControlType.kSmartMotion && mControlType != CANSparkBase.ControlType.kSmartMotion)
        {
            log(Level.INFO, "Starting motion magic");
            getActivePid().mProfiledPidController.reset(getPosition(), getVelocity());
        }

        mControlType = ctrl;

        // Give a bare bones sim for non-feedback related mode
        if (ctrl == CANSparkBase.ControlType.kDutyCycle)
        {
            mAppliedOutputSim.set(value);
            updateFollowers(value);
        }

        return REVLibError.kOk;
    }

    private double getArbPercentOutput(double arbFeedforward, int arbFFUnits)
    {
        double arbFFPercentage;
        if (arbFFUnits == SparkPIDController.ArbFFUnits.kPercentOut.value)
        {
            arbFFPercentage = arbFeedforward;
        }
        else if (arbFFUnits == SparkPIDController.ArbFFUnits.kVoltage.value)
        {
            arbFFPercentage = arbFeedforward / RobotController.getBatteryVoltage();
        }
        else
        {
            arbFFPercentage = 0;
            log(Level.SEVERE, "Unknown Arb FF unit: " + arbFFUnits);
        }

        return arbFFPercentage;
    }


    protected void addSimFollower(SimableCANSparkMax simableCANSparkMax)
    {
        mSparkMaxFollowers.add(simableCANSparkMax);
    }

    protected void addSimFollower(SimableCANSparkFlex motor)
    {
        mSparkFlexFollowers.add(motor);
    }

    public void updateSim()
    {
        double voltagePercentage = 0;

        switch (mControlType)
        {
        case kDutyCycle:
            voltagePercentage = mControlGoal;
            break;
        case kVelocity:
            voltagePercentage = calculateSpeedControl();
            break;
        case kPosition:
            voltagePercentage = calculatePositionControl();
            break;
        case kSmartMotion:
            voltagePercentage = calculateSmartMotionOutput();
            break;
        case kVoltage:
            voltagePercentage = mControlGoal / RobotController.getBatteryVoltage();
            break;
        default:
            log(Level.SEVERE, "Unsupported control type: " + mControlType);
            break;
        }

        mAppliedOutputSim.set(voltagePercentage);
        updateFollowers(voltagePercentage);
    }

    private void updateFollowers(double voltagePercentage)
    {
        for (SimableCANSparkMax follower : mSparkMaxFollowers) // NOPMD.CloseResource
        {
            follower.mSimHelper.mAppliedOutputSim.set(voltagePercentage);
        }
        for (SimableCANSparkFlex follower : mSparkFlexFollowers) // NOPMD.CloseResource
        {
            follower.mSimHelper.mAppliedOutputSim.set(voltagePercentage);
        }
    }

    private double constrainOutput(double inPercent)
    {
        return Math.min(1, Math.max(-1, inPercent));
    }


    private PIDFConstants getActivePid()
    {
        if (mPidConstants[mActivePidSlot] == null)
        {
            mPidConstants[mActivePidSlot] = new PIDFConstants(mActivePidSlot);
        }
        mPidConstants[mActivePidSlot].refreshValues(mActivePidSlot, mLatchedPidController);
        return mPidConstants[mActivePidSlot];
    }

    private double calculatePositionControl()
    {
        double goal = mControlGoal;
        PIDFConstants activePid = getActivePid();
        double output = activePid.mBasicPidController.calculate(getPosition(), goal);
        output += mArbFFPercentage;
        output = constrainOutput(output);
        log(Level.FINE, "Updating position control.... " + getPosition() + " vs " + goal + " -> " + output);
        return output;
    }

    @SuppressWarnings("PMD.ShortVariable")
    private double calculateSpeedControl()
    {
        double goal = mControlGoal;
        PIDFConstants activePid = getActivePid();
        double ff = goal * activePid.mF;
        double pid = activePid.mBasicPidController.calculate(getVelocity(), goal);
        double output = ff + pid + mArbFFPercentage;
        output = constrainOutput(output);
        log(Level.FINE, "Updating Speed control.... " + getVelocity() + " vs " + goal + " -> " + output + "(" + ff + " + " + pid + ")");

        return output;
    }

    private double calculateSmartMotionOutput()
    {
        double goal = mControlGoal;

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
        TrapezoidProfile.State setpoint = activePid.mProfiledPidController.getSetpoint();

        double ff = activePid.mF * setpoint.velocity; // NOPMD(ShortVariable)
        double output = constrainOutput(ff + pid + mArbFFPercentage);
        log(Level.FINE, "Updating MM control.... "
                + "Going to " + goal + "... "
                + "Position " + position + " vs " + setpoint.position + ", "
                + "Velocity " + getVelocity() + " vs " + setpoint.velocity + ", "
                + " -> output " + output + "(" + ff + " + " + pid + " + " + mArbFFPercentage + ")"
                + " constraints: Vel: " + activePid.mConstraints.maxVelocity + ", Accel: " + activePid.mConstraints.maxAcceleration);

        return output;
    }

    private double getVelocity()
    {
        return mLatchedEncoder.getVelocity();
    }

    private double getPosition()
    {
        return mLatchedEncoder.getPosition();
    }

    @SuppressWarnings("PMD.LinguisticNaming")
    public RelativeEncoder setLatchedEncoder(RelativeEncoder encoder)
    {
        mLatchedEncoder = encoder;
        return mLatchedEncoder;
    }

    protected void log(Level level, String message)
    {
        LOGGER.log(level, "REV Sim [" + mSparkMax.getDeviceId() + "] - " + message);
    }

}
