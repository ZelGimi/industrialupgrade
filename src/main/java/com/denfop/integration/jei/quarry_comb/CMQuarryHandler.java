package com.denfop.integration.jei.quarry_comb;


import com.denfop.IUCore;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CMQuarryHandler {

    private static final List<CMQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public CMQuarryHandler(ItemStack output) {
        this.output = output;
    }

    public static List<CMQuarryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CMQuarryHandler addRecipe(ItemStack output) {
        CMQuarryHandler recipe = new CMQuarryHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CMQuarryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CMQuarryHandler recipe : recipes) {
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
