package com.denfop.integration.jei.polymerizer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class PolymerizerHandler {

    private static final List<PolymerizerHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public PolymerizerHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<PolymerizerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PolymerizerHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        PolymerizerHandler recipe = new PolymerizerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("polymerizer")) {

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
