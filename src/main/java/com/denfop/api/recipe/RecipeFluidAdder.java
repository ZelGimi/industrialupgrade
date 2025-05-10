package com.denfop.api.recipe;

public class RecipeFluidAdder {

    private final String name;
    private final BaseFluidMachineRecipe baseMachineRecipe;

    public RecipeFluidAdder(String name, BaseFluidMachineRecipe baseMachineRecipe) {
        this.name = name;
        this.baseMachineRecipe = baseMachineRecipe;
    }

    public BaseFluidMachineRecipe getBaseMachineRecipe() {
        return baseMachineRecipe;
    }

    public String getName() {
        return name;
    }

}
