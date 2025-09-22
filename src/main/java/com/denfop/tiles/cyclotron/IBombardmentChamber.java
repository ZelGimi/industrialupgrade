package com.denfop.tiles.cyclotron;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;

public interface IBombardmentChamber extends IMultiElement, IUpdateTick {

    InventoryRecipes getInputSlot();

    MachineRecipe getOutput();

    int getChance();

    int getCryogen();

    int getPositrons();

}
