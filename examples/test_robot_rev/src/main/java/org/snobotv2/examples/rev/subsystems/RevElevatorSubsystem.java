package org.snobotv2.examples.rev.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SimableCANSparkMax;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.module_wrappers.rev.RevEncoderSimWrapper;
import org.snobotv2.module_wrappers.rev.RevMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.ElevatorSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class RevElevatorSubsystem extends SubsystemBase implements ElevatorSubsystem
{
    private final SimableCANSparkMax mLeadMotor; // NOPMD
    private final SimableCANSparkMax mFollowerMotor; // NOPMD
    private final CANEncoder mEncoder;
    private ISimWrapper mElevatorSim;

    public RevElevatorSubsystem()
    {
        mLeadMotor = new SimableCANSparkMax(BaseConstants.ELEVATOR_MOTOR_A, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerMotor = new SimableCANSparkMax(BaseConstants.ELEVATOR_MOTOR_B, CANSparkMaxLowLevel.MotorType.kBrushless);
        mFollowerMotor.follow(mLeadMotor);
        mEncoder = mLeadMotor.getEncoder();

        if (RobotBase.isSimulation())
        {
            mElevatorSim = new ElevatorSimWrapper(ElevatorSimConstants.createSim(),
                    new RevMotorControllerSimWrapper(mLeadMotor),
                    RevEncoderSimWrapper.create(mLeadMotor));
        }
    }

    @Override
    public void close()
    {
        mLeadMotor.close();
        mFollowerMotor.close();
    }

    @Override
    public void moveManually(double speed)
    {
        mLeadMotor.set(speed);
    }

    @Override
    public void goToPosition(double inches)
    {
//            double ticks = Units.inchesToMeters(inches) * 4096;
//            mMotor.set(ControlMode.Position, ticks);
    }

    @Override
    public void goToPositionMotionMagic(double inches)
    {
//            double ticks = Units.inchesToMeters(inches) * 4096;
//            mMotor.set(ControlMode.MotionMagic, ticks);
    }

    @Override
    public boolean isAtHeight(double inches, double allowableError)
    {
        return Math.abs(inches - getHeightInches()) < allowableError;
    }

    @Override
    public double getHeightInches()
    {
        return mEncoder.getPosition();
    }

    @Override
    public void simulationPeriodic()
    {
        mElevatorSim.update();
    }

    @Override
    public void stop()
    {
        mLeadMotor.set(0);
    }
}
