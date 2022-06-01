package com.denfop.integration.jei.cutting;


import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CuttingHandler {

    private static final List<CuttingHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;

    public CuttingHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<CuttingHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CuttingHandler addRecipe(ItemStack input, ItemStack output) {
        CuttingHandler recipe = new CuttingHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CuttingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CuttingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (MachineRecipe<IRecipeInput, Collection<ItemStack>> container : Recipes.metalformerCutting.getRecipes()) {
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
