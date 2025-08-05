package com.denfop.integration.jei.refrigeratorfluids;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorFluidsHandler {

    private static final List<RefrigeratorFluidsHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public RefrigeratorFluidsHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<RefrigeratorFluidsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RefrigeratorFluidsHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        RefrigeratorFluidsHandler recipe = new RefrigeratorFluidsHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("refrigerator")) {

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
