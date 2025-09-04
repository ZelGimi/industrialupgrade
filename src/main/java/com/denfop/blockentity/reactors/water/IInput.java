package com.denfop.blockentity.reactors.water;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;

public interface IInput extends MultiBlockElement {

    void addFluids(Fluids fluids);

    void clearList();

}
