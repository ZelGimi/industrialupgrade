package com.denfop.api.energy;

import java.util.ArrayList;

public class EnergyTickList<T extends EnergyTick> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof IEnergySource) {
            for (EnergyTick tick : this) {
                if (tick.getSource() == o) {
                    return true;
                }
            }
        } else {
            return super.contains(o);
        }
        return false;
    }

    @Override
    public boolean remove(final Object o) {
        if (o instanceof IEnergySource) {
            for (int i = 0; i < this.size(); i++) {
                EnergyTick tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }

    public EnergyTick removeSource(final Object o) {
        if (o instanceof IEnergySource) {
            for (int i = 0; i < this.size(); i++) {
                EnergyTick tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i);
                }
            }
        } else {
            return null;
        }
        return null;
    }

}
