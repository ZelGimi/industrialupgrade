package com.denfop.api.utilities;

import com.denfop.api.energy.EnergyTileInfo;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

public class AdvList<E> extends ArrayList<E> {

   public AdvList(){
       super();
   }
    public AdvList(int initialCapacity) {
        super(initialCapacity);
    }
    public AdvList(Collection<? extends E> c) {
        super(c);
    }
    public E get(Object o) {
        int index = indexOf(o);

        if(index == -1)
            return null;
        return get(index);
    }

}
