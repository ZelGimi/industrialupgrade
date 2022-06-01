package com.denfop.integration.jei.rolling;


import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RollingHandler {

    private static final List<RollingHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public RollingHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<RollingHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RollingHandler addRecipe(ItemStack input, ItemStack output) {
        RollingHandler recipe = new RollingHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RollingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RollingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.metalformerRolling.getRecipes()) {
            addRecipe(container.getInput().getInputs().get(0), new ArrayList<>(container.getOutput()).get(0)
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
