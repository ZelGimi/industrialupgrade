package com.denfop.integration.jei.extractor;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExtractorHandler {

    private static final List<ExtractorHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public ExtractorHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<ExtractorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ExtractorHandler addRecipe(ItemStack input, ItemStack output) {
        ExtractorHandler recipe = new ExtractorHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ExtractorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ExtractorHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("extractor")) {

            try {
                addRecipe(
                        container.input.getInputs().get(0).getInputs().get(0),
                        container.getOutput().items.get(0)
                );

            } catch (Exception ignored) {
            }
        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
