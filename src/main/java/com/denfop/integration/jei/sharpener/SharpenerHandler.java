package com.denfop.integration.jei.sharpener;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SharpenerHandler {

    private static final List<SharpenerHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public SharpenerHandler(
            ItemStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<SharpenerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SharpenerHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        SharpenerHandler recipe = new SharpenerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SharpenerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SharpenerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("sharpener")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }
    }


    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input);
    }

}
