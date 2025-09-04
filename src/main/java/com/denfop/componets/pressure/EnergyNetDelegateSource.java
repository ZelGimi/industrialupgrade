package com.denfop.componets.pressure;

import com.denfop.api.otherenergies.pressure.IPressureAcceptor;
import com.denfop.api.otherenergies.pressure.IPressureSource;
import com.denfop.componets.PressureComponent;
import net.minecraft.core.Direction;

public class EnergyNetDelegateSource extends EnergyNetDelegate implements IPressureSource {

    public EnergyNetDelegateSource(PressureComponent block) {
        super(block);
    }

    public int getSourceTier() {
        return this.buffer.sourceTier;
    }

    public boolean emitsPressureTo(IPressureAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }

    public double getOfferedPressure() {

        return this.buffer.storage;
    }


    public void drawPressure(double amount) {
    }

    @Override
    public boolean isAllowed() {
        return this.buffer.allow;
    }

    @Override
    public boolean setAllowed(final boolean allowed) {
        return this.buffer.allow = allowed;
    }


}
