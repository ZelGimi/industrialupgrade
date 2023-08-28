package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidTank;

public interface IBlastInputFluid extends IBlastPart {

    FluidTank getFluidTank();

    Fluids getFluid();

}
