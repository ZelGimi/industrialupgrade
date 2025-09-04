package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;

public interface IGenerator extends IMultiElement {

    ComponentBaseEnergy getEnergy();

    Fluids.InternalFluidTank getFluidTank();

}
