package org.snobotv2.examples.base_swerve.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base_swerve.Constants;
import org.snobotv2.module_wrappers.wpi.ADXRS450GyroWrapper;
import org.snobotv2.sim_wrappers.SwerveModuleSimWrapper;
import org.snobotv2.sim_wrappers.SwerveSimWrapper;

import java.util.List;

public class BaseSwerveDriveSubsystem extends SubsystemBase implements AutoCloseable
{
    // Distance between centers of right and left wheels on robot
    private static final double kTrackWidth = Units.inchesToMeters(20.733);

    // Distance between front and back wheels on robot
    private static final double kWheelBase = Units.inchesToMeters(20.733);

    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
            new Translation2d(kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxRotationRadiansPerSecond = Units.degreesToRadians(450);
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4.0);
    public static final double kWheelCircumfranceMeters = kWheelDiameterMeters * Math.PI;

    private final BaseSwerveModule mFrontLeft;
    private final BaseSwerveModule mRearLeft;
    private final BaseSwerveModule mFrontRight;
    private final BaseSwerveModule mRearRight;

    private final BaseSwerveModule[] mModules;

    private final ADXRS450_Gyro mGyro;

    private final Field2d mField;
    private final SwerveDriveOdometry mOdometry;
    private boolean mFieldRelative;

    private SwerveSimWrapper mSimulator;

    public interface ModuleFactory
    {
        BaseSwerveModule createModule(
                int driveMotorChannel,
                int turningMotorChannel,
                int turningEncoderChannels,
                double encoderOffset,
                String name);
    }

    public enum SwerveModulePosition
    {
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT
    }

    protected BaseSwerveDriveSubsystem(ModuleFactory factory, boolean addDebugTab)
    {
        mGyro = new ADXRS450_Gyro();
        mOdometry = new SwerveDriveOdometry(kDriveKinematics, mGyro.getRotation2d());

        mFrontLeft = factory.createModule(
                Constants.FRONT_LEFT_DRIVE_MOTOR_PORT,
                Constants.FRONT_LEFT_TURNING_MOTOR_PORT,
                Constants.FRONT_LEFT_TURNING_ENCODER_PORTS,
                0,
                "FL");

        mRearLeft = factory.createModule(
                Constants.REAR_LEFT_DRIVE_MOTOR_PORT,
                Constants.REAR_LEFT_TURNING_MOTOR_PORT,
                Constants.REAR_LEFT_TURNING_ENCODER_PORTS,
                0,
                "RL");

        mFrontRight = factory.createModule(
                Constants.FRONT_RIGHT_DRIVE_MOTOR_PORT,
                Constants.FRONT_RIGHT_TURNING_MOTOR_PORT,
                Constants.FRONT_RIGHT_TURNING_ENCODER_PORTS,
                0,
                "FR");

        mRearRight = factory.createModule(
                Constants.REAR_RIGHT_DRIVE_MOTOR_PORT,
                Constants.REAR_RIGHT_TURNING_MOTOR_PORT,
                Constants.REAR_RIGHT_TURNING_ENCODER_PORTS,
                0,
                "RR");

        mModules = new BaseSwerveModule[]{
            mFrontLeft,
            mFrontRight,
            mRearLeft,
            mRearRight
        };

        mField = new Field2d();

        // Shuffleboard debugging
        if (addDebugTab)
        {
            ShuffleboardTab debugTab = Shuffleboard.getTab("Drivetrain");

            debugTab.add("SwerveState", new SwerveModuleSendable());
            for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
            {
                debugTab.add(module.getName() + " Module", module);
            }
            debugTab.add(mField);
        }

        // Simulation
        if (RobotBase.isSimulation())
        {
            List<SwerveModuleSimWrapper> moduleSims = List.of(
                    mFrontLeft.getSimWrapper(),
                    mFrontRight.getSimWrapper(),
                    mRearLeft.getSimWrapper(),
                    mRearRight.getSimWrapper());
            mSimulator = new SwerveSimWrapper(kWheelBase, kTrackWidth, 64.0, 1.0, moduleSims, new ADXRS450GyroWrapper(mGyro));
        }
    }

    @Override
    public void close()
    {
        for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
        {
            module.close();
        }

        mGyro.close();
    }


    public List<SwerveModuleState> getModuleStates()
    {
        return List.of(
                mFrontLeft.getState(),
                mFrontRight.getState(),
                mRearLeft.getState(),
                mRearRight.getState());
    }

    public void stop()
    {
        for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
        {
            module.stop();
        }
    }


    public void setGoalPosition(Pose2d poseMeters)
    {
        mField.getObject("Goal").setPose(poseMeters);
    }


    private final class SwerveModuleSendable implements Sendable
    {
        @Override
        public void initSendable(SendableBuilder builder)
        {
            builder.setSmartDashboardType("SwerveDrive");
            for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
            {
                builder.addDoubleProperty(module.getName() + "/CurrentStateAngle", () -> module.getState().angle.getDegrees(), null);
                builder.addDoubleProperty(module.getName() + "/CurrentStateSpeed", () -> module.getState().speedMetersPerSecond, null);
                builder.addDoubleProperty(module.getName() + "/DesiredStateAngle", () -> module.getDesiredState().angle.getDegrees(), null);
                builder.addDoubleProperty(module.getName() + "/DesiredStateSpeed", () -> module.getDesiredState().speedMetersPerSecond, null);
                builder.addDoubleProperty(module.getName() + "/DrivePercentage", module::getDriveMotorPercentage, null);
                builder.addDoubleProperty(module.getName() + "/TurningPercentage", module::getTurningMotorPercentage, null);
            }
        }
    }

    public void manuallyMoveModule(SwerveModulePosition modulePosition, double turnSpeed, double driveSpeed)
    {
        mModules[modulePosition.ordinal()].setOpenLoop(turnSpeed, driveSpeed);
    }

    @Override
    public void periodic()
    {
        for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
        {
            module.periodic();
        }

        // Update the odometry in the periodic block
        if (RobotBase.isSimulation())
        {
            mOdometry.resetPosition(mSimulator.getPose(), mGyro.getRotation2d());
        } else
        {
            mOdometry.update(
                    mGyro.getRotation2d(),
                    mFrontLeft.getPosition(),
                    mFrontRight.getPosition(),
                    mRearLeft.getPosition(),
                    mRearRight.getPosition());
        }

        mField.setRobotPose(mOdometry.getPoseMeters());
    }

    @Override
    public void simulationPeriodic()
    {
        mSimulator.update();
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose()
    {
        return mOdometry.getPoseMeters();
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose)
    {
        mOdometry.resetPosition(pose, mGyro.getRotation2d());
    }

    public void setTeleDriveFieldRelative(boolean fieldRelative)
    {
        mFieldRelative = fieldRelative;
    }

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed Speed of the robot in the x direction (forward).
     * @param ySpeed Speed of the robot in the y direction (sideways).
     * @param rot    Angular rate of the robot.
     */
    @SuppressWarnings("ParameterName")
    public void teleDrive(double xSpeed, double ySpeed, double rot)
    {
        ChassisSpeeds chassisSpeed;
        if (mFieldRelative)
        {
            chassisSpeed = ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed,
                    ySpeed,
                    rot,
                    mGyro.getRotation2d());
        }
        else
        {
            chassisSpeed = new ChassisSpeeds(xSpeed, ySpeed, rot);
        }
        setModuleStates(kDriveKinematics.toSwerveModuleStates(chassisSpeed));
    }

    /**
     * Sets the swerve ModuleStates.
     *
     * @param desiredStates The desired SwerveModule states.
     */
    public void setModuleStates(SwerveModuleState... desiredStates)
    {
        SwerveDriveKinematics.desaturateWheelSpeeds(
                desiredStates, kMaxSpeedMetersPerSecond);
        for (int i = 0; i < mModules.length; ++i)
        {
            mModules[i].setDesiredState(desiredStates[i], true);
        }
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders()
    {
        for (BaseSwerveModule module : mModules) // NOPMD(CloseResource)
        {
            module.resetEncoders();
        }
    }
}
