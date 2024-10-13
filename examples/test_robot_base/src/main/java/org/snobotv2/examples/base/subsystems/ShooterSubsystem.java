package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.Subsystem;

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
                    LinearSystemId.createFlywheelSystem(kGearbox, kInertia, kGearing);
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
