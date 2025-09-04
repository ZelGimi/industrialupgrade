package com.denfop.componets.system;

import com.denfop.api.otherenergies.common.IAcceptor;
import com.denfop.api.otherenergies.common.IDual;
import com.denfop.api.otherenergies.common.IEmitter;
import com.denfop.api.otherenergies.common.ISource;
import com.denfop.componets.ComponentBaseEnergy;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

public class EnergyNetDelegateDual extends EnergyNetDelegate implements IDual {
    List<ISource> systemTicks = new LinkedList<>();


    public EnergyNetDelegateDual(ComponentBaseEnergy baseEnergy) {
        super(baseEnergy);
    }

    public boolean acceptsFrom(IEmitter emitter, Direction dir) {
        return this.sinkDirections.contains(dir);
    }

    public boolean emitsTo(IAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }


    public double canProvideEnergy() {
        return !this.sendingSidabled && !this.sourceDirections.isEmpty()
                ? this.getSourceEnergy()
                : 0.0D;
    }

    public int getSinkTier() {
        return this.buffer.sinkTier;
    }

    public int getSourceTier() {
        return this.buffer.sourceTier;
    }

    public double getDemanded() {
        return !this.receivingDisabled && this.buffer.storage < this.buffer.capacity
                ? this.buffer.capacity - this.buffer.storage
                : 0.0D;
    }

    @Override
    public void receivedEnergy(final double var2) {
        this.buffer.storage = this.buffer.storage + var2;
    }

    @Override
    public double getPerEnergy1() {
        return this.perenergy1;
    }

    @Override
    public double getPastEnergy1() {
        return this.pastEnergy1;
    }

    @Override
    public void setPastEnergy1(final double pastEnergy) {
        this.pastEnergy1 = pastEnergy;
    }

    @Override
    public void addPerEnergy1(final double setEnergy) {
        this.perenergy1 += setEnergy;
    }

    @Override
    public void addTick1(final double tick) {
        this.tick1 = tick;
    }

    @Override
    public double getTick1() {
        return this.tick1;
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

    @Override
    public void addTick(final double tick) {
        this.tick = tick;
    }

    @Override
    public double getTick() {
        return this.tick;
    }

    @Override
    public boolean isSink() {
        return true;
    }

    @Override
    public List<ISource> getEnergyTickList() {
        return systemTicks;
    }


}
