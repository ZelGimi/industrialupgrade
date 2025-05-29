package com.denfop.integration.jei.genetic_polymizer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneticPolymizerHandler {

    private static final List<GeneticPolymizerHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final ItemStack input, input1, input3, input4, input5, output;

    public GeneticPolymizerHandler(
            ItemStack input, ItemStack input1, ItemStack input3, ItemStack input4, ItemStack input5, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
        this.output = output;
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1, input3, input4, input5);
    }

    public static List<GeneticPolymizerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GeneticPolymizerHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input3, ItemStack input4, ItemStack input5, FluidStack input2,
            ItemStack output
    ) {
        GeneticPolymizerHandler recipe = new GeneticPolymizerHandler(input, input1, input3, input4, input5, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GeneticPolymizerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GeneticPolymizerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("genetic_polymerizer")) {

            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.input.getFluid(),

                    container.getOutput().items.get(0)
            );

        }
    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public FluidStack getInput2() {
        return input2;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public ItemStack getInput3() {
        return input3;
    }

    public ItemStack getInput4() {
        return input4;
    }

    public ItemStack getInput5() {
        return input5;
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
