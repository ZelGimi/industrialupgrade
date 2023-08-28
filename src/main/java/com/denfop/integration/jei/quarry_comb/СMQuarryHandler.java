package com.denfop.integration.jei.quarry_comb;


import com.denfop.IUCore;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class СMQuarryHandler {

    private static final List<СMQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public СMQuarryHandler(ItemStack output) {
        this.output = output;
    }

    public static List<СMQuarryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static СMQuarryHandler addRecipe(ItemStack output) {
        СMQuarryHandler recipe = new СMQuarryHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static СMQuarryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (СMQuarryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (ItemStack container : IUCore.get_comb_crushed) {
            addRecipe(container);

        }
    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == output.getItem();
    }

}
