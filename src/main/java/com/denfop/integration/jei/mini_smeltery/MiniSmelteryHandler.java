package com.denfop.integration.jei.mini_smeltery;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class MiniSmelteryHandler {

    private static final List<MiniSmelteryHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final ItemStack output;

    public MiniSmelteryHandler(
            FluidStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<MiniSmelteryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MiniSmelteryHandler addRecipe(
            FluidStack input,
            ItemStack output
    ) {
        MiniSmelteryHandler recipe = new MiniSmelteryHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("mini_smeltery")) {

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
