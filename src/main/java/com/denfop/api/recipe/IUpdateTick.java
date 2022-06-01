package com.denfop.api.recipe;

public interface IUpdateTick {

    void onUpdate();

    MachineRecipe getRecipeOutput();

    void setRecipeOutput(MachineRecipe output);

}
