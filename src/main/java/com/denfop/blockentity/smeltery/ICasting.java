package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.componets.ComponentProgress;
import com.denfop.inventory.Inventory;

public interface ICasting extends MultiBlockElement {

    ComponentProgress getProgress();

    FluidHandlerRecipe getFluid_handler();

    InventoryOutput getOutputSlot();

    Inventory getInputSlotB();

}
