package org.snobotv2.module_wrappers.wpi;

import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.simulation.OnboardIMUSim;
import org.snobotv2.module_wrappers.BaseGyroWrapper;

/**
 * Gyro wrapper for the Systemcore onboard IMU.
 *
 * Like the other gyro wrappers, the angle passed to {@link #setAngle(double)} is in degrees, clockwise positive. The
 * onboard IMU reports yaw in radians, counter-clockwise positive, so the value is converted before it is simulated.
 */
public class OnboardIMUWrapper extends BaseGyroWrapper
{
    public OnboardIMUWrapper(OnboardIMU imu)
    {
        super(angleDegrees -> OnboardIMUSim.setYaw(-Math.toRadians(angleDegrees)));
    }
}
