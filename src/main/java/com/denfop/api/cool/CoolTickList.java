package com.denfop.api.cool;



import java.util.ArrayList;

public class CoolTickList<T extends CoolTick<ICoolSource, Path>> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof ICoolSource) {
            for (CoolTick<ICoolSource, Path> tick : this) {
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
        if (o instanceof ICoolSource) {
            for (int i = 0; i < this.size(); i++) {
                CoolTick<ICoolSource, Path> tick = this.get(i);
                if (tick.getSource() == o) {
                    return this.remove(i) != null;
                }
            }
        } else {
            return super.remove(o);
        }
        return false;
    }
    public CoolTick<ICoolSource, Path> removeSource(final Object o) {
        if (o instanceof ICoolSource) {
            for (int i = 0; i < this.size(); i++) {
                CoolTick<ICoolSource, Path> tick = this.get(i);
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
