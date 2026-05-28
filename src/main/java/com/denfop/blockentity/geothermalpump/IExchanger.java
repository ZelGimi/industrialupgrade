package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;

public interface IExchanger extends IMultiElement {

    Fluids.InternalFluidTank getFluidTank();

}
