package org.snobotv2.examples.base.subsystems;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.system.LinearSystem;
import edu.wpi.first.wpilibj.system.plant.DCMotor;
import edu.wpi.first.wpilibj.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpiutil.math.numbers.N2;

public interface DrivetrainSubsystem extends Subsystem, AutoCloseable
{
    double getLeftDistance();

    double getRightDistance();

    double getLeftRate();

    double getRightRate();

    double getHeadingDegrees();

    void arcadeDrive(double speed, double rotation);

    void stop();

    void resetEncoders();

    void resetOdometry(Pose2d initialPose);

    Pose2d getPose();

    DifferentialDriveWheelSpeeds getWheelSpeeds();

    void tankDriveVolts(double left, double right);

    DrivetrainConstants getConstants();

    void driveDistance(double leftPosition, double rightPosition);

    void smartVelocityControlMetersPerSec(double leftVelocityMetersPerSec, double rightVelocityMetersPerSec);

    interface DrivetrainConstants
    {
        double getKsVolts();

        double getKvVoltSecondsPerMeter();

        double getKaVoltSecondsSquaredPerMeter();

        DifferentialDriveKinematics getKinematics();

        double getGearing();

        double getkWheelDiameterMeters();

        DifferentialDrivetrainSim createSim();
    }

    final class CimDrivetrainConstants implements DrivetrainConstants
    {
        private static final double ksVolts = 0.22;
        private static final double kvVoltSecondsPerMeter = 1.98;
        private static final double kaVoltSecondsSquaredPerMeter = 0.2;
        private static final double kvVoltSecondsPerRadian = 1.5;
        private static final double kaVoltSecondsSquaredPerRadian = 0.3;

        public static final LinearSystem<N2, N2, N2> kDrivetrainPlant =
                LinearSystemId.identifyDrivetrainSystem(kvVoltSecondsPerMeter, kaVoltSecondsSquaredPerMeter,
                        kvVoltSecondsPerRadian, kaVoltSecondsSquaredPerRadian);

        public static final DCMotor kDriveGearbox = DCMotor.getCIM(2);
        private static final double kDriveGearing = 8;

        private static final double kTrackwidthMeters = 0.69;
        private static final double kWheelDiameterMeters = 0.15;

        public static final DifferentialDriveKinematics kDriveKinematics =
                new DifferentialDriveKinematics(kTrackwidthMeters);


        public CimDrivetrainConstants()
        {
        }

        @Override
        public DifferentialDrivetrainSim createSim()
        {
            return new DifferentialDrivetrainSim(
                    kDrivetrainPlant,
                    kDriveGearbox,
                    kDriveGearing,
                    kTrackwidthMeters,
                    kWheelDiameterMeters / 2.0,
                    null);
        }

        @Override
        public double getGearing()
        {
            return kDriveGearing;
        }

        @Override
        public double getkWheelDiameterMeters()
        {
            return kWheelDiameterMeters;
        }

        @Override
        public double getKsVolts()
        {
            return ksVolts;
        }

        @Override
        public double getKvVoltSecondsPerMeter()
        {
            return kvVoltSecondsPerMeter;
        }

        @Override
        public double getKaVoltSecondsSquaredPerMeter()
        {
            return kaVoltSecondsSquaredPerMeter;
        }

        @Override
        public DifferentialDriveKinematics getKinematics()
        {
            return kDriveKinematics;
        }
    }


    final class NeoDrivetrainConstants implements DrivetrainConstants
    {
        private static final double ksVolts = 0.22;
        private static final double kvVoltSecondsPerMeter = Units.metersToInches(0.065_3);
        private static final double kaVoltSecondsSquaredPerMeter = Units.metersToInches(0.007_54);
        private static final double kvVoltSecondsPerRadian = 1.5;
        private static final double kaVoltSecondsSquaredPerRadian = 0.3;

        public static final LinearSystem<N2, N2, N2> kDrivetrainPlant =
                LinearSystemId.identifyDrivetrainSystem(kvVoltSecondsPerMeter, kaVoltSecondsSquaredPerMeter,
                        kvVoltSecondsPerRadian, kaVoltSecondsSquaredPerRadian);

        public static final DCMotor kDriveGearbox = DCMotor.getNEO(2);
        private static final double kDriveGearing = 40.0 / 10.0 * 34.0 / 20.0;

        private static final double kTrackwidthMeters = 1.155_488_171_380_903;
        private static final double kWheelDiameterInches = 4.0;
        private static final double kWheelDiameterMeters = Units.inchesToMeters(kWheelDiameterInches);

        public static final DifferentialDriveKinematics kDriveKinematics =
                new DifferentialDriveKinematics(kTrackwidthMeters);

        @Override
        public DifferentialDrivetrainSim createSim()
        {
            return new DifferentialDrivetrainSim(
                    kDrivetrainPlant,
                    kDriveGearbox,
                    kDriveGearing,
                    kTrackwidthMeters,
                    kWheelDiameterMeters / 2.0,
                    null);
        }

        public NeoDrivetrainConstants()
        {
        }

        @Override
        public double getGearing()
        {
            return kDriveGearing;
        }

        @Override
        public double getkWheelDiameterMeters()
        {
            return kWheelDiameterMeters;
        }

        @Override
        public double getKsVolts()
        {
            return ksVolts;
        }

        @Override
        public double getKvVoltSecondsPerMeter()
        {
            return kvVoltSecondsPerMeter;
        }

        @Override
        public double getKaVoltSecondsSquaredPerMeter()
        {
            return kaVoltSecondsSquaredPerMeter;
        }

        @Override
        public DifferentialDriveKinematics getKinematics()
        {
            return kDriveKinematics;
        }
    }
}
