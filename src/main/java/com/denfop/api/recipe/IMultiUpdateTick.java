package com.denfop.api.recipe;

import com.denfop.tiles.mechanism.EnumTypeMachines;

public interface IMultiUpdateTick extends IUpdateTick {

    MachineRecipe getRecipeOutput(int slotId);

    void setRecipeOutput(MachineRecipe output, int slotId);

    EnumTypeMachines getType();

}
