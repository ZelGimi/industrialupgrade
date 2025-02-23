package com.denfop.integration.jei.neutronseparator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NeutronSeparatorHandler {

    private static final List<NeutronSeparatorHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public NeutronSeparatorHandler(
            ItemStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<NeutronSeparatorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static NeutronSeparatorHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        NeutronSeparatorHandler recipe = new NeutronSeparatorHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static NeutronSeparatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (NeutronSeparatorHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("neutron_separator")) {
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
