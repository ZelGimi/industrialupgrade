package com.denfop.api.sytem;

import com.denfop.api.energy.SystemTick;

import java.util.ArrayList;

public class SystemTickList<T extends SystemTick<ISource, Path>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof ISource) {
            for (SystemTick<ISource, Path> tick : this) {
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
        if (o instanceof ISource) {
            for (int i = 0; i < this.size(); i++) {
                SystemTick<ISource, Path> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }

    public SystemTick<ISource, Path> removeSource(final Object o) {
        if (o instanceof ISource) {
            for (int i = 0; i < this.size(); i++) {
                SystemTick<ISource, Path> tick = this.get(i);
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
