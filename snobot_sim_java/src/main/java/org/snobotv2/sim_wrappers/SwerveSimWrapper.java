package org.snobotv2.sim_wrappers;

import edu.wpi.first.wpilibj.simulation.swerve.QuadSwerveSim;
import edu.wpi.first.wpilibj.simulation.swerve.SwerveModuleSim;
import org.snobotv2.interfaces.IGyroWrapper;

import java.util.ArrayList;
import java.util.List;

public class SwerveSimWrapper extends BaseSimWrapper
{
    private final QuadSwerveSim mSwerveSim;
    private final List<SwerveModuleSimWrapper> mModules;
    private final IGyroWrapper mGyro;

    public SwerveSimWrapper(
            double wheelBaseWidthM,
            double wheelBaseLengthM,
            double robotMasskg,
            double robotMOI,
            List<SwerveModuleSimWrapper> modules,
            IGyroWrapper gyro)
    {
        List<SwerveModuleSim> baseSims = new ArrayList<>();
        for (SwerveModuleSimWrapper module : modules)
        {
            baseSims.add(module.getBaseSim());
        }
        mSwerveSim = new QuadSwerveSim(wheelBaseWidthM, wheelBaseLengthM, robotMasskg, robotMOI, baseSims);
        mModules = modules;
        mGyro = gyro;
    }

    @Override
    public void update()
    {
        for (SwerveModuleSimWrapper module : mModules)
        {
            module.update();
        }
        mSwerveSim.update(mUpdatePeriod);
        mGyro.setAngle(-mSwerveSim.getCurPose().getRotation().getDegrees());
    }
}
