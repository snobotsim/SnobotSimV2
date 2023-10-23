package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ElevatorSubsystem extends Subsystem, AutoCloseable
{
    double DEFAULT_ALLOWABLE_HEIGHT_ERROR_INCHES = 1;

    final class ElevatorSimConstants
    {
        public static final double kElevatorGearing = 10.0;
        public static final double kCarriageMass = 4.0; // kg
        public static final double kMinElevatorHeight = 0.0;
        public static final double kMaxElevatorHeight = Units.inchesToMeters(100);
        public static final DCMotor kElevatorGearbox = DCMotor.getVex775Pro(4);

        public static final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

        public static ElevatorSim createSim()
        {
            return new ElevatorSim(
                    ElevatorSimConstants.kElevatorGearbox,
                    ElevatorSimConstants.kElevatorGearing,
                    ElevatorSimConstants.kCarriageMass,
                    ElevatorSimConstants.kElevatorDrumRadius,
                    ElevatorSimConstants.kMinElevatorHeight,
                    ElevatorSimConstants.kMaxElevatorHeight,
                    true,
                    0);
        }

        private ElevatorSimConstants()
        {
        }
    }

    void moveManually(double speed);

    void goToPosition(double inches);

    void goToPositionMotionMagic(double inches);

    double getHeightInches();

    boolean isAtLowerLimit();

    boolean isAtUpperLimit();

    default boolean isAtHeight(double inches)
    {
        return isAtHeight(inches, DEFAULT_ALLOWABLE_HEIGHT_ERROR_INCHES);
    }

    boolean isAtHeight(double inches, double allowableError);

    void stop();
}
