package org.snobotv2.module_wrappers.rev;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkSimHelper;
import edu.wpi.first.math.system.plant.DCMotor;
import org.snobotv2.module_wrappers.BaseMotorControllerWrapper;

public class RevMotorControllerSimWrapper extends BaseMotorControllerWrapper
{
    private final SparkSimHelper mSim;

    @Deprecated
    public RevMotorControllerSimWrapper(SparkBase motorController)
    {
        this(motorController, DCMotor.getNEO(1));
    }

    public RevMotorControllerSimWrapper(SparkBase motorController, DCMotor gearbox)
    {
        super(motorController.getDeviceId(), motorController::getAppliedOutput);
        mSim = new SparkSimHelper(motorController, gearbox);
    }

    @Override
    public void update()
    {
        mSim.updateSim();
    }

    @Override
    public void setCurrent(double current)
    {
        mSim.setCurrent(current);
    }
}
