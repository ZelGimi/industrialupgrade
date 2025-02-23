package com.denfop.integration.jei.genetic_transposer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GeneticTransposerHandler {

    private static final List<GeneticTransposerHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final ItemStack input, input1,input3,input4, output;

    public GeneticTransposerHandler(
            ItemStack input, ItemStack input1, ItemStack input3, ItemStack input4, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.output = output;
    }

    public static List<GeneticTransposerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GeneticTransposerHandler addRecipe(
            ItemStack input, ItemStack input1,  ItemStack input3, ItemStack input4, FluidStack input2,
            ItemStack output
    ) {
        GeneticTransposerHandler recipe = new GeneticTransposerHandler(input, input1,input3,input4, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GeneticTransposerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GeneticTransposerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("genetic_transposer")) {

            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
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

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
