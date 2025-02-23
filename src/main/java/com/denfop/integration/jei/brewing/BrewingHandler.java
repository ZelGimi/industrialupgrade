package com.denfop.integration.jei.brewing;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BrewingHandler {

    private static final List<BrewingHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, output;

    public BrewingHandler(ItemStack input, ItemStack input1, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
    }

    public static List<BrewingHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BrewingHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output) {
        BrewingHandler recipe = new BrewingHandler(input, input1, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static BrewingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (BrewingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("brewing")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
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

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
