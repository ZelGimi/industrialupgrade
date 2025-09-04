package com.denfop.blockentity.gaswell;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;

public interface ITank extends IMultiElement {

    Fluids.InternalFluidTank getTank();

}
