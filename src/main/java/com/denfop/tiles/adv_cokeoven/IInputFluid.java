package com.denfop.tiles.adv_cokeoven;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidTank;

public interface IInputFluid extends IMultiElement {

    FluidTank getFluidTank();

    Fluids getFluid();

}
