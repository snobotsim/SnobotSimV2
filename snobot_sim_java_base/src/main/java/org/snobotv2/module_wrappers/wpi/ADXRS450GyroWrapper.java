package org.snobotv2.module_wrappers.wpi;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

public class ADXRS450GyroWrapper extends BaseGyroWrapper
{
    public ADXRS450GyroWrapper(ADXRS450_Gyro gyro)
    {
        super(new ADXRS450_GyroSim(gyro)::setAngle);
    }
}
