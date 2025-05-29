package com.denfop.integration.jei;


import com.denfop.recipes.ScrapboxRecipeManager;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrapboxRecipeHandler {

    private static final List<ScrapboxRecipeHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final double need;

    public ScrapboxRecipeHandler(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        this.input = input;
        this.output = output;
        this.need = need;
    }

    public static List<ScrapboxRecipeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ScrapboxRecipeHandler addRecipe(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        ScrapboxRecipeHandler recipe = new ScrapboxRecipeHandler(input, output, need);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ScrapboxRecipeHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ScrapboxRecipeHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (final Map.Entry<ItemStack, Float> itemStackFloatEntry : ScrapboxRecipeManager.instance.getDrops().entrySet()) {
            addRecipe(itemStackFloatEntry.getKey(),itemStackFloatEntry.getKey(),itemStackFloatEntry.getValue());
        }
    }

    public double getNeed() {
        return need;
    }

    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
