package com.denfop.blockentity.mechanism.blastfurnace.api;

import com.denfop.componets.Fluids;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IBlastInputFluid extends IBlastPart {

    FluidTank getFluidTank();

    Fluids getFluid();

}
