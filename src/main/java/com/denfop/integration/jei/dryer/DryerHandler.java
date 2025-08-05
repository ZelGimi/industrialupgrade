package com.denfop.integration.jei.dryer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class DryerHandler {

    private static final List<DryerHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final ItemStack output;

    public DryerHandler(
            FluidStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<DryerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static DryerHandler addRecipe(
            FluidStack input,
            ItemStack output
    ) {
        DryerHandler recipe = new DryerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("dryer")) {

            addRecipe(
                    container.input.getInputs().get(0),
                    container.getOutput().items.get(0)
            );

        }
    }

    public FluidStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


}
