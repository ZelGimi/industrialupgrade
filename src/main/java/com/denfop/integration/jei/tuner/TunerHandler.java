package com.denfop.integration.jei.tuner;


import com.denfop.IUItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TunerHandler {

    private static final List<TunerHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public TunerHandler(ItemStack output) {
        this.output = output;
    }

    public static List<TunerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static TunerHandler addRecipe(ItemStack output) {
        TunerHandler recipe = new TunerHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static TunerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (TunerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(new ItemStack(IUItem.module7.getStack(10), 1));


    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
