package com.denfop.integration.jei.blastfurnace;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BFHandler {

    private static final List<BFHandler> recipes = new ArrayList<>();


    public BFHandler() {
    }

    public static List<BFHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BFHandler addRecipe() {
        BFHandler recipe = new BFHandler();
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static BFHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (BFHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(
        );


    }


}
