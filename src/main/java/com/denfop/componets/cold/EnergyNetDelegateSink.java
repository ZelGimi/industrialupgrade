package com.denfop.componets.cold;

import com.denfop.api.otherenergies.cool.ICoolSink;
import com.denfop.api.otherenergies.cool.ICoolSource;
import com.denfop.componets.CoolComponent;

import java.util.LinkedList;
import java.util.List;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements ICoolSink {

    List<ICoolSource> list = new LinkedList<>();


    public EnergyNetDelegateSink(CoolComponent coolComponent) {
        super(coolComponent);
    }


    public double getDemandedCool() {
        if (this.buffer.storage != 0) {
            return 64;
        } else {
            return 0;
        }
    }

    public void receivedCold(double amount) {
        if (amount > 0) {
            this.buffer.storage -= 0.05 * amount / 4;
            if (this.buffer.storage < 0)
                this.buffer.storage = 0;
        }

    }

    @Override
    public boolean needCooling() {
        return this.buffer.storage > 0;
    }

    @Override
    public List<ICoolSource> getEnergyTickList() {
        return list;
    }


}