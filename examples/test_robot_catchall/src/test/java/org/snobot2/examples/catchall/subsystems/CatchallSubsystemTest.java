package org.snobot2.examples.catchall.subsystems;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snobotv2.SimDeviceDumpHelper;
import org.snobotv2.examples.catchall.subsystems.CatchallSubsystem;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Tag("flaky") // Causes CI timeouts for linux
public class CatchallSubsystemTest
{
    @Test
    public void testConstruction()
    {
        assertDoesNotThrow(CatchallSubsystem::new);
        SimDeviceDumpHelper.dumpSimDevices();
    }
}
