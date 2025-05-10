package com.denfop.integration.jei.convertermatter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConverterHandler {

    private static final List<ConverterHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public ConverterHandler(ItemStack output) {
        this.output = output;
    }

    public static List<ConverterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ConverterHandler addRecipe(ItemStack output) {
        ConverterHandler recipe = new ConverterHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ConverterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ConverterHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("converter")) {
            addRecipe(
                    container.getOutput().items.get(0)
            );


        }

    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

}
