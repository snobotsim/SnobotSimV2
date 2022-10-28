package edu.wpi.first.wpilibj.simulation.swerve;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

class MotorGearboxWheelSim {
    private final DCMotor m_motor;
    private final double m_gearRatio;
    private final double m_wheelRadiusM;
    private final double m_gearboxFricCoefNmPerRadPerSec;

    private double m_curGroundForceN;
    private double m_wheelRotationsRad;
    private double m_prevWheelRotationalSpeedRadPerSec;

    private double m_wheelSpeedRPM;
    private double m_motorSpeedRPM;

    /**
     * 1D simulation of a motor, gearbox, and wheel.
     *      Provides an output torque, given an input speed and voltage.
     * @param motor Motor set used to provide power input.
     * @param gearRatio Gearbox Ratio. Gearboxes which slow
     *      down motors (ie all of them) should have this greater than 1.0.
     * @param wheelRadiusM Radius of the wheel in meters
     * @param gearboxFricCoefNmPerRadPerSec Kinetic Friction Losses
     *      in the gearbox (expressed in units of Nm of "fighting" force per
     *      radian per second of motor speed). Set to 0 if you're awesome
     *      with white lithium grease, make it positive if your team maybe
     *      forget the grease sometimes.
     */
    MotorGearboxWheelSim(DCMotor motor,
                         double gearRatio,
                         double wheelRadiusM,
                         double gearboxFricCoefNmPerRadPerSec) {
        this.m_motor = motor;
        this.m_gearRatio = gearRatio;
        this.m_wheelRadiusM = wheelRadiusM;
        this.m_gearboxFricCoefNmPerRadPerSec = gearboxFricCoefNmPerRadPerSec;
    }

    private double groundVelocityMPS;

    public void update(double groundVelocityMPS, double motorVoltage, double dtSeconds) {
        this.groundVelocityMPS = groundVelocityMPS;

        double wheelRotSpd = groundVelocityMPS / m_wheelRadiusM;
        double motorRotSpd = wheelRotSpd * m_gearRatio;

        double motorTq = m_motor.KtNMPerAmp * m_motor.getCurrent(motorRotSpd,
                                                                    motorVoltage);

        double gboxFricTq = motorRotSpd * m_gearboxFricCoefNmPerRadPerSec;

        //div by 1/torque ratio, or mul by gear ratio
        double curWheelTq = motorTq * m_gearRatio - gboxFricTq;

        m_curGroundForceN = curWheelTq / m_wheelRadiusM / 2;

        //Trapezoidal integration
        m_wheelRotationsRad += (wheelRotSpd + m_prevWheelRotationalSpeedRadPerSec) / 2
                              * dtSeconds;

        m_prevWheelRotationalSpeedRadPerSec = wheelRotSpd;

//        System.out.println("******" + groundVelocityMPS + ", " + wheelRotSpd + ", " + motorRotSpd);

        m_wheelSpeedRPM = Units.radiansPerSecondToRotationsPerMinute(wheelRotSpd);
        m_motorSpeedRPM = Units.radiansPerSecondToRotationsPerMinute(motorRotSpd);
    }

    public double getPositionRev() {
        return m_wheelRotationsRad / 2 / Math.PI;
    }

    public double getGroundForce_N() {
        return m_curGroundForceN;
    }

    public double getWheelSpeed_RPM() {
        return m_wheelSpeedRPM;
    }

    public double getWheelSpeedMetersPerSecond() {
        return groundVelocityMPS;
    }

    public double getMotorSpeed_RPM() {
        return m_motorSpeedRPM;
    }

}
