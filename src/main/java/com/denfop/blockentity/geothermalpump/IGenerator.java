package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;

public interface IGenerator extends MultiBlockElement {

    ComponentBaseEnergy getEnergy();

    Fluids.InternalFluidTank getFluidTank();

}
