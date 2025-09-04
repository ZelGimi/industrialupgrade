package com.denfop.blockentity.cyclotron;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;

public interface IBombardmentChamber extends MultiBlockElement, IUpdateTick {

    InventoryRecipes getInputSlot();

    MachineRecipe getOutput();

    int getChance();

    int getCryogen();

    int getPositrons();

}
