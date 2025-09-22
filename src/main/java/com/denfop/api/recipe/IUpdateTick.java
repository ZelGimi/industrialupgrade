package com.denfop.api.recipe;

public interface IUpdateTick {

    void onUpdate();

    MachineRecipe getRecipeOutput();

    void setRecipeOutput(MachineRecipe output);

    ;

    default MachineRecipe getRecipeOutput(int i) {
        return getRecipeOutput();
    }

    default void setRecipeOutput(int i, MachineRecipe output) {
        setRecipeOutput(output);
    }

    ;
}
