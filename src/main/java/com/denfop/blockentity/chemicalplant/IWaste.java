package com.denfop.blockentity.chemicalplant;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;

public interface IWaste extends IMultiElement {

    Fluids.InternalFluidTank getFluidTank();

}
