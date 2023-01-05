package com.denfop.api.recipe;

public interface IMultiUpdateTick extends IUpdateTick {

    MachineRecipe getRecipeOutput(int slotId);

    void setRecipeOutput(MachineRecipe output, int slotId);


}
