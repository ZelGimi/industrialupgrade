package com.denfop.blockentity.reactors.water;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;

public interface IOutput extends MultiBlockElement {

    void addFluids(Fluids fluids);

    void clearList();

}
