package com.denfop.integration.jei.fquarry;


import com.denfop.IUCore;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FQuarryHandler {

    private static final List<FQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public FQuarryHandler(ItemStack output) {
        this.output = output;
    }

    public static List<FQuarryHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FQuarryHandler addRecipe(ItemStack output) {
        FQuarryHandler recipe = new FQuarryHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static FQuarryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (FQuarryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (ItemStack container : IUCore.get_ingot) {
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
