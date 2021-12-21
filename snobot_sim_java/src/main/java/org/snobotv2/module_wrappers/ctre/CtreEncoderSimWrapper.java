package org.snobotv2.module_wrappers.ctre;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.snobotv2.module_wrappers.BaseEncoderWrapper;

public class CtreEncoderSimWrapper extends BaseEncoderWrapper
{
    public CtreEncoderSimWrapper(WPI_TalonSRX talonSrx)
    {
        this(talonSrx, 1.0 / 4096);
    }

    public CtreEncoderSimWrapper(WPI_TalonFX talonSrx)
    {
        this(talonSrx, 1.0 / 4096);
    }

    public CtreEncoderSimWrapper(WPI_TalonSRX talonSrx, double ticksToPosition)
    {
        super(
                () -> talonSrx.getSelectedSensorPosition() * ticksToPosition,
                (double position) -> talonSrx.getSimCollection().setQuadratureRawPosition((int) (position / ticksToPosition)),
                (double velocity) -> talonSrx.getSimCollection().setQuadratureVelocity((int) (velocity * 100))
        );
    }

    public CtreEncoderSimWrapper(WPI_TalonFX talonFx, double ticksToPosition)
    {
        super(
                () -> talonFx.getSelectedSensorPosition() * ticksToPosition,
                (double position) -> talonFx.getSimCollection().setIntegratedSensorRawPosition((int) (position / ticksToPosition)),
                (double velocity) -> talonFx.getSimCollection().setIntegratedSensorVelocity((int) (velocity * 100))
        );
    }
}
