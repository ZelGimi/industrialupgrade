package com.denfop.tiles.base;

import java.util.ArrayList;

public class DataOreList<T extends DataOre> extends ArrayList<T> {

    @Override
    public boolean contains(final Object o) {
        if (o instanceof String) {
            for (DataOre tick : this) {
                if (tick.getName().equals(o)) {
                    return true;
                }
            }
        } else {
            return super.contains(o);
        }
        return false;
    }

    public DataOre get(final String o) {
        for (DataOre tick : this) {
            if (tick.getName().equals(o)) {
                return tick;
            }
        }
        return null;
    }


}
