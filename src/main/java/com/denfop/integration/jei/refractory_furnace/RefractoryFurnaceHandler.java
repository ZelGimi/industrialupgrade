package com.denfop.integration.jei.refractory_furnace;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class RefractoryFurnaceHandler {

    private static final List<RefractoryFurnaceHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final ItemStack input, output;

    public RefractoryFurnaceHandler(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
    }

    public static List<RefractoryFurnaceHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RefractoryFurnaceHandler addRecipe(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        RefractoryFurnaceHandler recipe = new RefractoryFurnaceHandler(input, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RefractoryFurnaceHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RefractoryFurnaceHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("elec_refractory_furnace")) {

            addRecipe(container.input.getInputs().get(0).getInputs().get(0), container.input.getFluid(),

                    container.getOutput().items.get(0)
            );

        }
    }


    public ItemStack getInput() {
        return input;
    }

    public FluidStack getInput2() {
        return input2;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
