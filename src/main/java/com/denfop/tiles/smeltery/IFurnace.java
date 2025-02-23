package com.denfop.tiles.smeltery;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.ComponentProgress;

public interface IFurnace extends IUpdateTick, IMultiElement {

    InvSlotRecipes getInvSlot();

    MachineRecipe getOutput();

    ComponentProgress getComponent();

    boolean isActive();

    void setActive(boolean active);

    boolean isChangeRecipe();

    void setChangeRecipe(final boolean changeRecipe);

}
