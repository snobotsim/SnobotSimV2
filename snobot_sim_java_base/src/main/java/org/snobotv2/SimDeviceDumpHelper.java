package org.snobotv2;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI.SimDeviceInfo;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

public final class SimDeviceDumpHelper
{
    private SimDeviceDumpHelper()
    {
        // Nothing to do
    }

    @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
    public static void dumpSimDevices()
    {
        StringBuilder builder = new StringBuilder(200);
        builder.append("***************************************************\nDumping devices:\n");
        for (SimDeviceInfo deviceInfo : SimDeviceSim.enumerateDevices(""))
        {
            builder.append("Got a device: \n").append("  ").append(deviceInfo.name).append('\n');
            for (SimDeviceDataJNI.SimValueInfo valueInfo : SimDeviceDataJNI.enumerateSimValues(deviceInfo.handle))
            {
                builder
                    .append("    ")
                    .append(valueInfo.name)
                    .append(", direction: ")
                    .append(valueInfo.direction)
                    .append(", type: ")
                    .append(valueInfo.value.getType())
                    .append(" -> ")
                    .append(getValue(valueInfo))
                    .append('\n');
            }
        }

        builder.append("***************************************************\n");
        System.out.println(builder); // NOPMD.SystemPrintln
    }

    private static String getValue(SimDeviceDataJNI.SimValueInfo valueInfo)
    {
        String output;
        switch (valueInfo.value.getType())
        {
        case HALValue.kBoolean:
            output = String.valueOf(valueInfo.value.getBoolean());
            break;
        case HALValue.kDouble:
            output = String.valueOf(valueInfo.value.getDouble());
            break;
        case HALValue.kEnum:
            output = "(enum) " + valueInfo.value.getNativeLong();
            break;
        case HALValue.kInt:
            output = String.valueOf((int) valueInfo.value.getLong());
            break;
        case HALValue.kLong:
            output = String.valueOf(valueInfo.value.getLong());
            break;
        default:
            output = "UNKNOWN";
            break;
        }

        return output;
    }
}
