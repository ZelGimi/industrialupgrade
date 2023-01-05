package com.denfop.integration.jei.antiupgradeblock;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AntiUpgradeBlockHandler {

    private static final List<AntiUpgradeBlockHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public AntiUpgradeBlockHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<AntiUpgradeBlockHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AntiUpgradeBlockHandler addRecipe(ItemStack input, ItemStack output) {
        AntiUpgradeBlockHandler recipe = new AntiUpgradeBlockHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AntiUpgradeBlockHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (AntiUpgradeBlockHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("antiupgradeblock")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


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
