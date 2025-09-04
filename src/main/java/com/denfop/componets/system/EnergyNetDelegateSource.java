package com.denfop.componets.system;

import com.denfop.api.otherenergies.common.IAcceptor;
import com.denfop.api.otherenergies.common.ISource;
import com.denfop.componets.ComponentBaseEnergy;
import net.minecraft.core.Direction;

public class EnergyNetDelegateSource extends EnergyNetDelegate implements ISource {


    public EnergyNetDelegateSource(ComponentBaseEnergy baseEnergy) {
        super(baseEnergy);
    }

    public int getSourceTier() {
        return this.buffer.sourceTier;
    }

    public boolean emitsTo(IAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }


    public double canProvideEnergy() {
        assert !this.sourceDirections.isEmpty();

        return !this.sendingSidabled ? this.getSourceEnergy() : 0.0D;
    }

    public void extractEnergy(double amount) {
        assert amount <= this.buffer.storage;
        this.buffer.storage -= amount;
    }

    @Override
    public double getPerEnergy() {
        return this.perenergy;
    }

    @Override
    public double getPastEnergy() {
        return this.pastEnergy;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        this.perenergy += setEnergy;
    }

    @Override
    public boolean isSource() {
        return true;
    }


}
