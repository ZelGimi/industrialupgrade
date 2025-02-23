package com.denfop.tiles.smeltery;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.ComponentProgress;
import com.denfop.invslot.InvSlot;

public interface ICasting extends IMultiElement {

    ComponentProgress getProgress();

    FluidHandlerRecipe getFluid_handler();

    InvSlotOutput getOutputSlot();

    InvSlot getInputSlotB();

}
