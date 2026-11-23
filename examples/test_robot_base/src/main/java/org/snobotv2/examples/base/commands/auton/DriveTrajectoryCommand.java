package org.snobotv2.examples.base.commands.auton;

import org.wpilib.command2.Command;
import org.wpilib.command2.InstantCommand;
import org.wpilib.math.controller.LTVUnicycleController;
import org.wpilib.math.controller.PIDController;
import org.wpilib.math.controller.SimpleMotorFeedforward;
import org.wpilib.math.kinematics.DifferentialDriveKinematics;
import org.wpilib.math.kinematics.DifferentialDriveWheelVelocities;
import org.wpilib.math.trajectory.DrivetrainSplineTrajectory;
import org.wpilib.system.Timer;
import org.snobotv2.examples.base.subsystems.DrivetrainSubsystem;
import org.snobotv2.examples.base.trajectories.TrajectoryFactory;

public final class DriveTrajectoryCommand
{
    private static final double LOOP_PERIOD = 0.02;

    public static Command createWithVoltage(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory)
    {
        return createWithVoltage(drivetrain, trajectory, true);
    }

    public static Command createWithVoltage(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory, boolean resetOnStart)
    {
        return wrap(drivetrain, trajectory, new FollowTrajectoryCommand(drivetrain, trajectory, true), resetOnStart);
    }

    public static Command createWithVelocity(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory)
    {
        return createWithVelocity(drivetrain, trajectory, true);
    }

    public static Command createWithVelocity(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory, boolean resetOnStart)
    {
        return wrap(drivetrain, trajectory, new FollowTrajectoryCommand(drivetrain, trajectory, false), resetOnStart);
    }

    private static Command wrap(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory, Command followCommand, boolean resetOnStart)
    {
        Command runThenStop = followCommand.andThen(drivetrain::stop);

        if (resetOnStart)
        {
            Command resetPose = new InstantCommand(() -> drivetrain.resetOdometry(trajectory.start().pose));
            return resetPose.andThen(runThenStop);
        }
        return runThenStop;
    }

    /**
     * Follows a trajectory with an LTV unicycle controller. In voltage mode the wheel velocities are tracked with a
     * feedforward plus PID, like the old WPILib RamseteCommand did. In velocity mode they are handed to the drivetrain's
     * smart velocity control.
     */
    private static final class FollowTrajectoryCommand extends Command
    {
        private final DrivetrainSubsystem mDrivetrain;
        private final DrivetrainSplineTrajectory mTrajectory;
        private final boolean mUseVoltage;
        private final LTVUnicycleController mFollower;
        private final DifferentialDriveKinematics mKinematics;
        private final SimpleMotorFeedforward mFeedforward;
        private final PIDController mLeftController;
        private final PIDController mRightController;
        private final Timer mTimer;

        private DifferentialDriveWheelVelocities mPrevVelocities;
        private double mPrevTime;

        private FollowTrajectoryCommand(DrivetrainSubsystem drivetrain, DrivetrainSplineTrajectory trajectory, boolean useVoltage)
        {
            DrivetrainSubsystem.DrivetrainConstants constants = drivetrain.getConstants();
            mDrivetrain = drivetrain;
            mTrajectory = trajectory;
            mUseVoltage = useVoltage;
            mFollower = new LTVUnicycleController(LOOP_PERIOD);
            mKinematics = constants.getKinematics();
            mFeedforward = new SimpleMotorFeedforward(
                    constants.getKsVolts(),
                    constants.getKvVoltSecondsPerMeter(),
                    constants.getKaVoltSecondsSquaredPerMeter());
            mLeftController = new PIDController(TrajectoryFactory.DriveConstants.kPDriveVel, 0, 0);
            mRightController = new PIDController(TrajectoryFactory.DriveConstants.kPDriveVel, 0, 0);
            mTimer = new Timer();

            addRequirements(drivetrain);
        }

        @Override
        public void initialize()
        {
            mPrevTime = -1;
            mPrevVelocities = new DifferentialDriveWheelVelocities();
            mLeftController.reset();
            mRightController.reset();
            mTimer.reset();
            mTimer.start();
        }

        @Override
        public void execute()
        {
            double curTime = mTimer.get();

            if (mPrevTime < 0)
            {
                mPrevTime = curTime;
                stopMotors();
                return;
            }

            DifferentialDriveWheelVelocities target = mKinematics.toWheelVelocities(
                    mFollower.calculate(mDrivetrain.getPose(), mTrajectory.sampleAt(curTime)));

            if (mUseVoltage)
            {
                double leftFeedforward = mFeedforward.calculate(mPrevVelocities.left, target.left);
                double rightFeedforward = mFeedforward.calculate(mPrevVelocities.right, target.right);

                DifferentialDriveWheelVelocities measured = mDrivetrain.getWheelSpeeds();
                double leftOutput = leftFeedforward + mLeftController.calculate(measured.left, target.left);
                double rightOutput = rightFeedforward + mRightController.calculate(measured.right, target.right);
                mDrivetrain.tankDriveVolts(leftOutput, rightOutput);
            }
            else
            {
                mDrivetrain.smartVelocityControlMetersPerSec(target.left, target.right);
            }

            mPrevVelocities = target;
            mPrevTime = curTime;
        }

        @Override
        public void end(boolean interrupted)
        {
            mTimer.stop();
            if (interrupted)
            {
                stopMotors();
            }
        }

        @Override
        public boolean isFinished()
        {
            return mTimer.hasElapsed(mTrajectory.duration());
        }

        private void stopMotors()
        {
            if (mUseVoltage)
            {
                mDrivetrain.tankDriveVolts(0, 0);
            }
            else
            {
                mDrivetrain.smartVelocityControlMetersPerSec(0, 0);
            }
        }
    }

    private DriveTrajectoryCommand()
    {
    }
}
