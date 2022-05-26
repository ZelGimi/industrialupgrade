package com.denfop.api.recipe;

import ic2.api.recipe.RecipeOutput;

public class BaseMachineRecipe {

    public final IInput input;
    public final RecipeOutput output;

    public BaseMachineRecipe(IInput input, RecipeOutput output){
        this.input = input;
        this.output=output;
    }
    public RecipeOutput getOutput(IInput input){
        if(this.input.equals(input))
            return this.output;

        return null;
    }

}
