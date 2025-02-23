package com.denfop.api.transport;

import com.denfop.api.energy.SystemTick;

import java.util.ArrayList;

public class TransportTickList<T extends TransportTick<ITransportSource, Path>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof ITransportSource) {
            for (TransportTick<ITransportSource, Path> tick : this) {
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
        if (o instanceof ITransportSource) {
            for (int i = 0; i < this.size(); i++) {
                TransportTick<ITransportSource, Path> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }
    public TransportTick<ITransportSource, Path> removeSource(final Object o) {
        if (o instanceof ITransportSource) {
            for (int i = 0; i < this.size(); i++) {
                TransportTick<ITransportSource, Path> tick = this.get(i);
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
