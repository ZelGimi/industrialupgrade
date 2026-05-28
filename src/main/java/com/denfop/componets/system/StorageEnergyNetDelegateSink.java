package com.denfop.componets.system;

import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.*;
import com.denfop.componets.ComponentBaseEnergy;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StorageEnergyNetDelegateSink extends EnergyNetDelegate implements ISink, IConductor {


    List<ISource> systemTicks = new LinkedList<>();

    ConductorInfo conductorInfo;
    InfoCable infoCable;


    public StorageEnergyNetDelegateSink(ComponentBaseEnergy baseEnergy) {
        super(baseEnergy);
        conductorInfo = new ConductorInfo(baseEnergy.getParent().pos, this, EnergyType.STORAGE);
    }

    @Override
    public List<ISource> getEnergyTickList() {
        return systemTicks;
    }

    public boolean acceptsFrom(IEmitter emitter, Direction direction) {
        return true;
    }

    public boolean emitsTo(IAcceptor receiver, Direction direction) {
        return true;
    }

    public double getDemanded() {
        return !this.receivingDisabled && this.buffer.storage < this.buffer.capacity
                ? this.buffer.capacity - this.buffer.storage
                : 0.0D;
    }

    public void receivedEnergy(double amount) {
        this.buffer.storage = this.buffer.storage + amount;
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
    public double getConductorBreakdownEnergy(EnergyType type) {
        return Integer.MAX_VALUE;
    }

    @Override
    public InfoCable getCable(EnergyType type) {
        return infoCable;
    }

    @Override
    public void setCable(EnergyType type, InfoCable cable) {
        this.infoCable = cable;
    }

    @Override
    public void removeConductor() {

    }

    @Override
    public EnergyType getEnergyType() {
        return EnergyType.STORAGE;
    }

    @Override
    public boolean hasEnergies() {
        return false;
    }

    @Override
    public List<EnergyType> getEnergies() {
        return Collections.singletonList(EnergyType.STORAGE);
    }

    @Override
    public ConductorInfo getInfo(EnergyType energyType) {
        return conductorInfo;
    }
}
