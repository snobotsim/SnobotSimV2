package org.snobotv2.examples.ctre.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.snobotv2.examples.base.BaseConstants;
import org.snobotv2.examples.base.subsystems.ElevatorSubsystem;
import org.snobotv2.module_wrappers.ctre.CtreDigitalInputWrapper;
import org.snobotv2.module_wrappers.ctre.CtreEncoderSimWrapper;
import org.snobotv2.module_wrappers.ctre.CtreMotorControllerSimWrapper;
import org.snobotv2.sim_wrappers.ElevatorSimWrapper;
import org.snobotv2.sim_wrappers.ISimWrapper;

public class CtreElevatorSubsystem extends SubsystemBase implements ElevatorSubsystem
{
    private static final double kTicksPerRotation = 4096;

    private final WPI_TalonSRX mLeadTalon;
    private final WPI_TalonSRX mFollowerTalon; // NOPMD
    private ISimWrapper mElevatorSim;

    public CtreElevatorSubsystem()
    {
        mLeadTalon = new WPI_TalonSRX(BaseConstants.ELEVATOR_MOTOR_A);
        mFollowerTalon = new WPI_TalonSRX(BaseConstants.ELEVATOR_MOTOR_B);

        mLeadTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 5);
        mLeadTalon.config_kP(0, 0.08);
        mFollowerTalon.follow(mLeadTalon);

        if (RobotBase.isSimulation())
        {
            ElevatorSimWrapper elevatorSim = new ElevatorSimWrapper(ElevatorSimConstants.createSim(),
                    new CtreMotorControllerSimWrapper(mLeadTalon),
                    new CtreEncoderSimWrapper(mLeadTalon));
            elevatorSim.setLowerLimitSwitch(new CtreDigitalInputWrapper(mLeadTalon, false));
            elevatorSim.setUpperLimitSwitch(new CtreDigitalInputWrapper(mLeadTalon, true));

            mElevatorSim = elevatorSim;
        }
    }

    @Override
    public void close()
    {
        mLeadTalon.free();
        mFollowerTalon.free();
    }

    @Override
    public void moveManually(double speed)
    {
        mLeadTalon.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public void goToPosition(double inches)
    {
        double ticks = Units.inchesToMeters(inches) * kTicksPerRotation;
        mLeadTalon.set(ControlMode.Position, ticks);
    }

    @Override
    public void goToPositionMotionMagic(double inches)
    {
        double ticks = Units.inchesToMeters(inches) * kTicksPerRotation;
        mLeadTalon.set(ControlMode.MotionMagic, ticks);
    }

    @Override
    public double getHeightInches()
    {
        double meters = mLeadTalon.getSelectedSensorPosition() / kTicksPerRotation;
        return Units.metersToInches(meters);
    }

    @Override
    public boolean isAtHeight(double inches, double allowableError)
    {
        return Math.abs(inches - getHeightInches()) < allowableError;
    }

    @Override
    public void simulationPeriodic()
    {
        mElevatorSim.update();
    }

    @Override
    public void stop()
    {
        mLeadTalon.set(0);
    }


    @Override
    public boolean isAtLowerLimit()
    {
        return mLeadTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    @Override
    public boolean isAtUpperLimit()
    {
        return mLeadTalon.getSensorCollection().isFwdLimitSwitchClosed();
    }
}
