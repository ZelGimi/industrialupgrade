package com.denfop.integration.jei.vein;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VeinHandler {

    private static final List<VeinHandler> recipes = new ArrayList<>();


    public VeinHandler(

    ) {

    }

    public static List<VeinHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static VeinHandler addRecipe(
    ) {
        VeinHandler recipe = new VeinHandler();
        recipes.add(recipe);
        return recipe;
    }

    public static VeinHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (VeinHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        addRecipe();

    }


}
