package com.denfop.integration.jei.genaddstone;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GenAddStoneHandler {

    private static final List<GenAddStoneHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, output;

    public GenAddStoneHandler(ItemStack input, ItemStack input1, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
    }

    public static List<GenAddStoneHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenAddStoneHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output) {
        GenAddStoneHandler recipe = new GenAddStoneHandler(input, input1, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenAddStoneHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenAddStoneHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {

        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("genadditionstone")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0)
            );
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    new ItemStack(Blocks.STONE, 8,3)
            );
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    new ItemStack(Blocks.STONE, 8,5)
            );
        }

    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
