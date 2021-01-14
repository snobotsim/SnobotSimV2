package com.revrobotics;

import com.revrobotics.jni.CANSparkMaxJNI;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
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

    protected class PIDFConstants
    {
        protected final PIDController mBasicPidController;
        protected final ProfiledPIDController mProfiledPidController;
        protected TrapezoidProfile.Constraints mConstraints;
        public double mF;

        public PIDFConstants()
        {
            mConstraints = new TrapezoidProfile.Constraints(0, 0);
            mBasicPidController = new PIDController(0, 0, 0);
            mProfiledPidController = new ProfiledPIDController(0, 0, 0, mConstraints);
            mF = 0;
        }

        private void refreshValues(int slotId)
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

            double maxVelocity = CANSparkMaxJNI.c_SparkMax_GetSmartMotionMaxVelocity(m_sparkMax, slotId);
            double maxAcceleration = CANSparkMaxJNI.c_SparkMax_GetSmartMotionMaxAccel(m_sparkMax, slotId);
            mConstraints = new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration);
            mProfiledPidController.setConstraints(mConstraints);
        }

        public void reset()
        {
            mBasicPidController.reset();
            mProfiledPidController.reset(0);
        }
    }

    private ControlType mControlType;
    private double mControlGoal;
    private double mArbFFPercentage;

    private final CANEncoder mEncoder;
    private final SimDouble mAppliedOutputSim;
    private final List<SimableCANSparkMax> mFollowers;


    private final PIDFConstants[] mPidConstants;
    private int mActivePidSlot;

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
            mPidConstants[i] = new PIDFConstants();
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
    CANError setpointCommand(double value, ControlType ctrl, int pidSlot, double arbFeedforward, int arbFFUnits)
    {
        if (RobotBase.isReal())
        {
            return super.setpointCommand(value, ctrl, pidSlot, arbFeedforward, arbFFUnits);
        }

        if (arbFFUnits == CANPIDController.ArbFFUnits.kPercentOut.value)
        {
            mArbFFPercentage = arbFeedforward;
        }
        else if (arbFFUnits == CANPIDController.ArbFFUnits.kVoltage.value)
        {
            mArbFFPercentage = arbFeedforward / RobotController.getBatteryVoltage();
        }
        else
        {
            mArbFFPercentage = 0;
            log(Level.SEVERE, "Unknown Arb FF unit: " + arbFFUnits);
        }

        mControlType = ctrl;
        mActivePidSlot = pidSlot;

        mControlGoal = value;

        // Give a bare bones sim for non-feedback related mode
        if (ctrl == ControlType.kDutyCycle)
        {
            mAppliedOutputSim.set(value);
            updateFollowers(value);
        }

        return CANError.kOk;
    }

    //////////////////////////////////////////////////


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
        mPidConstants[mActivePidSlot].refreshValues(mActivePidSlot);
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

    private double calculateSpeedControl()
    {
        double goal = mControlGoal;
        PIDFConstants activePid = getActivePid();
        double ff = goal * activePid.mF;
        double pid = activePid.mBasicPidController.calculate(getVelocity(), goal);
        double output = ff + pid;
        output = constrainOutput(output);
        log(Level.FINE, "Updating Speed control.... " + getVelocity() + " vs " + goal + " -> " + output + "(" + ff + " + " + pid + ")");

        return output;
    }

    private double calculateSmartMotionOutput()
    {
        double goal = mControlGoal;

        PIDFConstants activePid = getActivePid();

        double position = getPosition();

        double pid = activePid.mProfiledPidController.calculate(position, goal);
        TrapezoidProfile.State setpoint = activePid.mProfiledPidController.getSetpoint();

        double ff = activePid.mF * setpoint.velocity;
        double output = constrainOutput(ff + pid);
        log(Level.FINE, "Updating MM control.... "
                + "Going to " + goal + "... "
                + "Position " + position + " vs " + setpoint.position + ", "
                + "Velocity " + getVelocity() + " vs " + setpoint.velocity + ", "
                + " -> output " + output + "(" + ff + " + " + pid + ")"
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
