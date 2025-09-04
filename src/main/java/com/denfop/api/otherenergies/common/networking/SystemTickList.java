package com.denfop.api.otherenergies.common.networking;

import com.denfop.api.energy.SystemTick;
import com.denfop.api.otherenergies.common.interfaces.Source;

import java.util.ArrayList;

public class SystemTickList<T extends SystemTick<Source, Path>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof Source) {
            for (SystemTick<Source, Path> tick : this) {
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
        if (o instanceof Source) {
            for (int i = 0; i < this.size(); i++) {
                SystemTick<Source, Path> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }

    public SystemTick<Source, Path> removeSource(final Object o) {
        if (o instanceof Source) {
            for (int i = 0; i < this.size(); i++) {
                SystemTick<Source, Path> tick = this.get(i);
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
