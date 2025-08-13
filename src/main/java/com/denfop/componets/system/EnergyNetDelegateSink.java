package com.denfop.componets.system;

import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.sytem.IEmitter;
import com.denfop.api.sytem.ISink;
import com.denfop.api.sytem.ISource;
import com.denfop.componets.ComponentBaseEnergy;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;
public class EnergyNetDelegateSink extends EnergyNetDelegate implements ISink {


    List<ISource> systemTicks = new LinkedList<>();


    public EnergyNetDelegateSink(ComponentBaseEnergy baseEnergy) {
        super(baseEnergy);
    }



    @Override
    public List<ISource> getEnergyTickList() {
        return systemTicks;
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



}
