package com.denfop.tiles.adv_cokeoven;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IInputFluid extends IMultiElement {

    FluidTank getFluidTank();

    Fluids getFluid();

}
