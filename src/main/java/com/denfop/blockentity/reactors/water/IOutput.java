package com.denfop.blockentity.reactors.water;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;

public interface IOutput extends IMultiElement {

    void addFluids(Fluids fluids);

    void clearList();

}
