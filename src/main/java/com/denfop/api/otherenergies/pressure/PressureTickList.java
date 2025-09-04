package com.denfop.api.otherenergies.pressure;


import java.util.ArrayList;

public class PressureTickList<T extends PressureTick<IPressureSource, PressurePath>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof IPressureSource) {
            for (PressureTick<IPressureSource, PressurePath> tick : this) {
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
        if (o instanceof IPressureSource) {
            for (int i = 0; i < this.size(); i++) {
                PressureTick<IPressureSource, PressurePath> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }

    public PressureTick<IPressureSource, PressurePath> removeSource(final Object o) {
        if (o instanceof IPressureSource) {
            for (int i = 0; i < this.size(); i++) {
                PressureTick<IPressureSource, PressurePath> tick = this.get(i);
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
