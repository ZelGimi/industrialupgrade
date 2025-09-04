package com.denfop.componets.heat;

import com.denfop.api.otherenergies.heat.IHeatSink;
import com.denfop.api.otherenergies.heat.IHeatSource;
import com.denfop.componets.HeatComponent;

import java.util.LinkedList;
import java.util.List;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements IHeatSink {

    List<IHeatSource> list = new LinkedList<>();


    public EnergyNetDelegateSink(HeatComponent block) {
        super(block);
    }


    @Override
    public List<IHeatSource> getEnergyTickList() {
        return list;
    }


    public double getDemandedHeat() {

        return this.buffer.capacity;
    }


    public void receivedHeat(double amount) {
        this.setHeatStored(amount);

    }

    @Override
    public boolean needTemperature() {
        return this.buffer.need;
    }

    public void setHeatStored(double amount) {
        if (this.buffer.storage < amount) {
            this.buffer.storage = amount;
        }
    }


}