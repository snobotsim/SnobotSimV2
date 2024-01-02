package org.snobotv2.examples.phoenix5_swerve;

/**
 * A collection of static utility methods.
 */
public final class CtreUtils
{
    private CtreUtils()
    {
    }

    /***
     * Converts from degrees to Falcon encoder position
     *
     * @param degrees Input degrees
     * @param gearRatio Mechanism gear ratio
     * @return Falcon encoder position
     */
    public static double degreesToFalcon(double degrees, double gearRatio)
    {
        return degrees / (360.0 / (gearRatio * 2048));
    }

    /**
     * @param counts    Falcon Counts
     * @param gearRatio Gear Ratio between Falcon and Mechanism
     * @return Degrees of Rotation of Mechanism
     */
    public static double falconToDegrees(double counts, double gearRatio)
    {
        return counts * (360.0 / (gearRatio * 2048.0));
    }

    /***
     * Converts RPM to Falcon Encoder Counts per 100 ms.
     *
     * @param rpm Input RPM
     * @param gearRatio Mechanism RPM
     * @return Falcon Velocity
     */
    public static double rpmToFalcon(double rpm, double gearRatio)
    {
        double motorRPM = rpm * gearRatio;
        return motorRPM * (2048.0 / 600.0);
    }

    public static double falconToRpm(double velocityCounts, double gearRatio)
    {
        double motorRPM = velocityCounts * (600.0 / 2048.0);
        return motorRPM / gearRatio;
    }

    public static double mpsToFalcon(double velocity, double circumference, double gearRatio)
    {
        double wheelRPM = (velocity * 60) / circumference;
        return rpmToFalcon(wheelRPM, gearRatio);
    }

    public static double falconToMps(double velocityCounts, double circumference, double gearRatio)
    {
        double wheelRPM = falconToRpm(velocityCounts, gearRatio);
        return (wheelRPM * circumference) / 60;
    }
}
