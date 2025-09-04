package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.ComponentProgress;

public interface IFurnace extends IUpdateTick, MultiBlockElement {

    InventoryRecipes getInvSlot();

    MachineRecipe getOutput();

    ComponentProgress getComponent();

    boolean isActive();

    void setActive(boolean active);

    boolean isChangeRecipe();

    void setChangeRecipe(final boolean changeRecipe);

}
