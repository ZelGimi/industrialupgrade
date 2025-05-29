package com.denfop.integration.jei.cyclotron;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CyclotronHandler {

    private static final List<CyclotronHandler> recipes = new ArrayList<>();
    private final int percent;
    private final ItemStack input, output;

    public CyclotronHandler(ItemStack input, ItemStack output, int percent) {
        this.input = input;
        this.output = output;
        this.percent = percent;
    }

    public static List<CyclotronHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CyclotronHandler addRecipe(ItemStack input, ItemStack output, int percent) {
        CyclotronHandler recipe = new CyclotronHandler(input, output, percent);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CyclotronHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CyclotronHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("cyclotron")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().metadata.getInt("chance")
            );


        }

    }


    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public int getPercent() {
        return percent;
    }


}
