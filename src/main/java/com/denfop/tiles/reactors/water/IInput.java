package com.denfop.tiles.reactors.water;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;

public interface IInput extends IMultiElement {

    void addFluids(Fluids fluids);

    void clearList();

}
