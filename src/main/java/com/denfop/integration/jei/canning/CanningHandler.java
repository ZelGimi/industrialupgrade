package com.denfop.integration.jei.canning;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CanningHandler {

    private static final List<CanningHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, output;
    private final FluidStack fluidStack;


    public CanningHandler(ItemStack input, ItemStack input1, FluidStack fluidStack, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.fluidStack = fluidStack;
        this.output = output;
    }

    public static List<CanningHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CanningHandler addRecipe(ItemStack input, ItemStack input1, FluidStack fluidStack, ItemStack output) {
        CanningHandler recipe = new CanningHandler(input, input1, fluidStack, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CanningHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CanningHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("cannerenrich")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getFluid(),
                    container.getOutput().items.get(0)
            );


        }
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
