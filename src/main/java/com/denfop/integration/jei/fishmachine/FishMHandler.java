package com.denfop.integration.jei.fishmachine;


import com.denfop.IUCore;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FishMHandler {

    private static final List<FishMHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public FishMHandler(ItemStack output) {
        this.output = output;
    }

    public static List<FishMHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FishMHandler addRecipe(ItemStack output) {
        FishMHandler recipe = new FishMHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static FishMHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (FishMHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {


        for (ItemStack container : IUCore.fish_rodding) {
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
