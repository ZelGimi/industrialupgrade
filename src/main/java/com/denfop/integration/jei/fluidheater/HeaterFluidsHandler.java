package com.denfop.integration.jei.fluidheater;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class HeaterFluidsHandler {

    private static final List<HeaterFluidsHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public HeaterFluidsHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<HeaterFluidsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static HeaterFluidsHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        HeaterFluidsHandler recipe = new HeaterFluidsHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("heat")) {

            addRecipe(
                    container.input.getInputs().get(0),
                    container.getOutput_fluid().get(0)
            );

        }
    }

    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
