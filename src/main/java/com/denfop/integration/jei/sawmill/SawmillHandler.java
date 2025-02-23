package com.denfop.integration.jei.sawmill;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SawmillHandler {

    private static final List<SawmillHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public SawmillHandler(
            ItemStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<SawmillHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SawmillHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        SawmillHandler recipe = new SawmillHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SawmillHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SawmillHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("sawmill")) {
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
