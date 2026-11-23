package org.snobotv2;

import org.wpilib.hardware.hal.HALValue;
import org.wpilib.hardware.hal.simulation.SimDeviceDataJNI;
import org.wpilib.hardware.hal.simulation.SimDeviceDataJNI.SimDeviceInfo;
import org.wpilib.simulation.SimDeviceSim;

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
        case HALValue.BOOLEAN:
            output = String.valueOf(valueInfo.value.getBoolean());
            break;
        case HALValue.DOUBLE:
            output = String.valueOf(valueInfo.value.getDouble());
            break;
        case HALValue.ENUM:
            output = "(enum) " + valueInfo.value.getNativeLong();
            break;
        case HALValue.INT:
            output = String.valueOf((int) valueInfo.value.getLong());
            break;
        case HALValue.LONG:
            output = String.valueOf(valueInfo.value.getLong());
            break;
        default:
            output = "UNKNOWN";
            break;
        }

        return output;
    }
}
