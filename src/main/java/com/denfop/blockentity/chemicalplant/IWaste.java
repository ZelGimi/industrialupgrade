package com.denfop.blockentity.chemicalplant;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;

public interface IWaste extends MultiBlockElement {

    Fluids.InternalFluidTank getFluidTank();

}
