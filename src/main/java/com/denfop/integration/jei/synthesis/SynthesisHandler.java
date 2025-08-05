package com.denfop.integration.jei.synthesis;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SynthesisHandler {

    private static final List<SynthesisHandler> recipes = new ArrayList<>();
    private final int percent;
    private final ItemStack input, input1, output;
    private final BaseMachineRecipe container;

    public SynthesisHandler(ItemStack input, ItemStack input1, ItemStack output, int percent, BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.percent = percent;
        this.container = container;
    }

    public static List<SynthesisHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SynthesisHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, int percent, BaseMachineRecipe container) {
        SynthesisHandler recipe = new SynthesisHandler(input, input1, output, percent, container);
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
                    container.getOutput().metadata.getInt("percent"), container
            );


        }

    }

    public BaseMachineRecipe getContainer() {
        return container;
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
        return true;
    }

}
