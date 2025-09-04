package com.denfop.componets.heat;

import com.denfop.api.otherenergies.heat.IHeatAcceptor;
import com.denfop.api.otherenergies.heat.IHeatSource;
import com.denfop.componets.HeatComponent;
import net.minecraft.core.Direction;

public class EnergyNetDelegateSource extends EnergyNetDelegate implements IHeatSource {


    public EnergyNetDelegateSource(HeatComponent block) {
        super(block);
    }


    public boolean emitsHeatTo(IHeatAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }

    public double getOfferedHeat() {

        return this.buffer.storage;
    }


    public void drawHeat(double amount) {
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
