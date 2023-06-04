package com.denfop.api.recipe;

public class RecipeAdder {

    private final String name;
    private final BaseMachineRecipe baseMachineRecipe;

    public RecipeAdder(String name, BaseMachineRecipe baseMachineRecipe) {
        this.name = name;
        this.baseMachineRecipe = baseMachineRecipe;
    }

    public BaseMachineRecipe getBaseMachineRecipe() {
        return baseMachineRecipe;
    }

    public String getName() {
        return name;
    }

}
