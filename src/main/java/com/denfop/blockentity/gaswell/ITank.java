package com.denfop.blockentity.gaswell;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;

public interface ITank extends MultiBlockElement {

    Fluids.InternalFluidTank getTank();

}
