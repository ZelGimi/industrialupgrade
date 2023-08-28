package com.denfop.integration.jei.bf;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlastFHandler {

    private static final List<BlastFHandler> recipes = new ArrayList<>();


    public BlastFHandler() {
    }

    public static List<BlastFHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BlastFHandler addRecipe() {
        BlastFHandler recipe = new BlastFHandler();
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static BlastFHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (BlastFHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(
        );


    }


}
