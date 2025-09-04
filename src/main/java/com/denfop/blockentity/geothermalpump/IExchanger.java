package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;

public interface IExchanger extends MultiBlockElement {

    Fluids.InternalFluidTank getFluidTank();

}
