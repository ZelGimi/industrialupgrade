package com.denfop.integration.jei.solidmatters;


import com.denfop.Config;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MatterHandler {

    private static final List<MatterHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final int energy;

    public MatterHandler(ItemStack input) {
        this.input = input;
        this.energy = (int) 5E7D;
    }

    public static List<MatterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MatterHandler addRecipe(ItemStack input) {
        MatterHandler recipe = new MatterHandler(input);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MatterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MatterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("matter")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0)
            );


        }

    }

    public int getEnergy() {
        return energy;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
