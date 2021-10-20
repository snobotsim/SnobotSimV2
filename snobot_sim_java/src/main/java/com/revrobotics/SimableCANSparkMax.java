package com.revrobotics;

import com.revrobotics.jni.CANSparkMaxJNI;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SimableCANSparkMax extends CANSparkMax
{
    private static final Logger LOGGER = Logger.getLogger(SimableCANSparkMax.class.getName());
    private static final int NUM_PID_SLOTS = 4;

    private ControlType mControlType;
    private double mControlGoal;
    private double mArbFFPercentage;

    private SimablePidController mLatchedPidController; 
    private final CANEncoder mEncoder;
    private final SimDouble mAppliedOutputSim;
    private final List<SimableCANSparkMax> mFollowers;


    private final SimableCANSparkMax.PIDFConstants[] mPidConstants;
    private int mActivePidSlot;

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
            double kp = CANSparkMaxJNI.c_SparkMax_GetP(m_sparkMax, slotId);
            double ki = CANSparkMaxJNI.c_SparkMax_GetI(m_sparkMax, slotId);
            double kd = CANSparkMaxJNI.c_SparkMax_GetD(m_sparkMax, slotId);
            mF = CANSparkMaxJNI.c_SparkMax_GetFF(m_sparkMax, slotId);

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

    private static final class SimablePidController extends CANPIDController
    {
        private final double[] mMaxAccel = new double[4];
        private final double[] mMaxVel = new double[4];

        public SimablePidController(CANSparkMax device)
        {
            super(device);
        }

        // These don't seem to work with the provided sim

        @Override
        public CANError setSmartMotionMaxAccel(double maxAccel, int slotID)
        {
            if (RobotBase.isReal())
            {
                return super.setSmartMotionMaxAccel(maxAccel, slotID);
            }

            mMaxAccel[slotID] = maxAccel;
            return CANError.kOk;
        }

        @Override
        public CANError setSmartMotionMaxVelocity(double maxVel, int slotID)
        {
            if (RobotBase.isReal())
            {
                return super.setSmartMotionMaxVelocity(maxVel, slotID);
            }

            mMaxVel[slotID] = maxVel;
            return CANError.kOk;
        }

        @Override
        public double getSmartMotionMaxAccel(int slotID)
        {
            if (RobotBase.isReal())
            {
                return super.getSmartMotionMaxAccel(slotID);
            }
            return mMaxAccel[slotID];
        }

        @Override
        public double getSmartMotionMaxVelocity(int slotID)
        {
            if (RobotBase.isReal())
            {
                return super.getSmartMotionMaxVelocity(slotID);
            }
            return mMaxVel[slotID];
        }
    }

    /**
     * Create a new SPARK MAX Controller
     *
     * @param deviceID The device ID.
     * @param type     The motor type connected to the controller. Brushless motors
     *                 must be connected to their matching color and the hall sensor
     *                 plugged in. Brushed motors must be connected to the Red and
     */
    public SimableCANSparkMax(int deviceID, MotorType type)
    {
        super(deviceID, type);

        SimDeviceSim deviceSim = new SimDeviceSim("SPARK MAX [" + deviceID + "]");
        mAppliedOutputSim = deviceSim.getDouble("Applied Output");
        mFollowers = new ArrayList<>();
        mControlType = ControlType.kDutyCycle;

        mEncoder = getEncoder();

        mPidConstants = new PIDFConstants[NUM_PID_SLOTS];
        for (int i = 0; i < NUM_PID_SLOTS; ++i)
        {
            mPidConstants[i] = null;
        }
    }

    //////////////////////////////////////////////////
    // Base class hijacking

    @Override
    public void close()
    {
        CANSparkMaxJNI.c_SparkMax_Destroy(m_sparkMax);
    }

    @Override
    public CANError follow(final CANSparkMax leader)
    {
        if (RobotBase.isReal())
        {
            return super.follow(leader);
        }

        if (leader instanceof SimableCANSparkMax)
        {
            ((SimableCANSparkMax) leader).addSimFollower(this);
        }

        return CANError.kOk;
    }

    @Override
    public CANPIDController getPIDController()
    {
        if (RobotBase.isReal())
        {
            return super.getPIDController();
        }
        mLatchedPidController = new SimablePidController(this);
        return mLatchedPidController;
    }

    @Override
    /* default */ CANError setpointCommand(double value, ControlType ctrl, int pidSlot, double arbFeedforward, int arbFFUnits)
    {
        if (RobotBase.isReal())
        {
            return super.setpointCommand(value, ctrl, pidSlot, arbFeedforward, arbFFUnits);
        }

        mActivePidSlot = pidSlot;
        mArbFFPercentage = getArbPercentOutput(arbFeedforward, arbFFUnits);
        mControlGoal = value;

        if (ctrl == ControlType.kSmartMotion && mControlType != ControlType.kSmartMotion)
        {
            log(Level.INFO, "Starting motion magic");
            getActivePid().mProfiledPidController.reset(getPosition(), getVelocity());
        }

        mControlType = ctrl;

        // Give a bare bones sim for non-feedback related mode
        if (ctrl == ControlType.kDutyCycle)
        {
            mAppliedOutputSim.set(value);
            updateFollowers(value);
        }

        return CANError.kOk;
    }

    //////////////////////////////////////////////////

    private double getArbPercentOutput(double arbFeedforward, int arbFFUnits)
    {
        double arbFFPercentage;
        if (arbFFUnits == CANPIDController.ArbFFUnits.kPercentOut.value)
        {
            arbFFPercentage = arbFeedforward;
        }
        else if (arbFFUnits == CANPIDController.ArbFFUnits.kVoltage.value)
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

    
    private void addSimFollower(SimableCANSparkMax simableCANSparkMax)
    {
        mFollowers.add(simableCANSparkMax);
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
        for (SimableCANSparkMax follower : mFollowers) // NOPMD.CloseResource
        {
            follower.mAppliedOutputSim.set(voltagePercentage);
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
        return mEncoder.getVelocity();
    }

    private double getPosition()
    {
        return mEncoder.getPosition();
    }


    protected void log(Level level, String message)
    {
        LOGGER.log(level, "REV Sim [" + getDeviceId() + "] - " + message);
    }

}
