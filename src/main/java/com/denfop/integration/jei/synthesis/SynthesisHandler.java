package com.denfop.integration.jei.synthesis;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SynthesisHandler {

    private static final List<SynthesisHandler> recipes = new ArrayList<>();
    private final int percent;
    private final ItemStack input, input1, output;

    public SynthesisHandler(ItemStack input, ItemStack input1, ItemStack output, int percent) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.percent = percent;
    }

    public static List<SynthesisHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SynthesisHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, int percent) {
        SynthesisHandler recipe = new SynthesisHandler(input, input1, output, percent);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SynthesisHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SynthesisHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("synthesis")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().metadata.getInteger("percent")
            );


        }

    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public int getPercent() { // Получатель выходного предмета рецепта.
        return percent;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
