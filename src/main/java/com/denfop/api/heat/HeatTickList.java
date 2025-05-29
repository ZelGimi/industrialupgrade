package com.denfop.api.heat;


import java.util.ArrayList;

public class HeatTickList<T extends HeatTick<IHeatSource, Path>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof IHeatSource) {
            for (HeatTick<IHeatSource, Path> tick : this) {
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
        if (o instanceof IHeatSource) {
            for (int i = 0; i < this.size(); i++) {
                HeatTick<IHeatSource, Path> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }

    public HeatTick<IHeatSource, Path> removeSource(final Object o) {
        if (o instanceof IHeatSource) {
            for (int i = 0; i < this.size(); i++) {
                HeatTick<IHeatSource, Path> tick = this.get(i);
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
