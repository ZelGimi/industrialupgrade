package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.api.recipe.InvSlotOutput;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import net.minecraftforge.fluids.FluidTank;

public interface IBlastInputFluid extends IBlastPart {

    FluidTank getFluidTank();

    InvSlotOutput getInvSlotOutput();

    InvSlotConsumableLiquidByList getInvSlotConsumableLiquidBy();

    Fluids getFluid();

}
