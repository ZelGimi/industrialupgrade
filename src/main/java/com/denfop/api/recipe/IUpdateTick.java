package com.denfop.api.recipe;

public interface IUpdateTick {

    void onUpdate();

    MachineRecipe getRecipeOutput();
    default MachineRecipe getRecipeOutput(int i ){
        return getRecipeOutput();
    };

    void setRecipeOutput(MachineRecipe output);

   default void setRecipeOutput(int i ,MachineRecipe output){
         setRecipeOutput(output);
    };
}
