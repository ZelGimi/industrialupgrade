package com.denfop.integration.jei.quarry_mac;


import com.denfop.IUCore;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MQuarryHandler {

    private static final List<MQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public MQuarryHandler(ItemStack output) {
        this.output = output;
    }

    public static List<MQuarryHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MQuarryHandler addRecipe(ItemStack output) {
        MQuarryHandler recipe = new MQuarryHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MQuarryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MQuarryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (ItemStack container : IUCore.get_crushed) {
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
