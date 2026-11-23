package org.snobotv2.examples.base.subsystems;

import org.wpilib.math.numbers.N1;
import org.wpilib.math.system.LinearSystem;
import org.wpilib.math.system.Models;
import org.wpilib.simulation.FlywheelSim;
import org.wpilib.math.system.DCMotor;
import org.wpilib.command2.Subsystem;

public interface ShooterSubsystem extends Subsystem, AutoCloseable
{
    final class FlywheelSimConstants
    {
        public static final DCMotor kGearbox = DCMotor.getVex775Pro(2);
        public static final double kGearing = 4;
        public static final double kInertia = 0.008;

        public static FlywheelSim createSim()
        {
            LinearSystem<N1, N1, N1> plant =
                    Models.flywheelFromPhysicalConstants(kGearbox, kInertia, kGearing);
            return new FlywheelSim(plant, kGearbox);
        }

        private FlywheelSimConstants()
        {
        }
    }

    void spinAtRpm(double rpm);

    double getRPM();

    void stop();
}
