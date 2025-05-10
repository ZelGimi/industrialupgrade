package com.denfop.tiles.cyclotron;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;

public interface IBombardmentChamber extends IMultiElement, IUpdateTick {

    InvSlotRecipes getInputSlot();

    MachineRecipe getOutput();

    int getChance();

    int getCryogen();

    int getPositrons();

}
