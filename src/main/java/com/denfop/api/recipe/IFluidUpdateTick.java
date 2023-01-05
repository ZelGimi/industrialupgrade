package com.denfop.api.recipe;

public interface IFluidUpdateTick {

    void onUpdate();

    BaseFluidMachineRecipe getRecipeOutput();

    void setRecipeOutput(BaseFluidMachineRecipe output);

}
