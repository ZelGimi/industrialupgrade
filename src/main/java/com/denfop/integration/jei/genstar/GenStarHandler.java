package com.denfop.integration.jei.genstar;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GenStarHandler {

    private static final List<GenStarHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, input2, input3, input4, input5, input6, output;

    public GenStarHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4,
            ItemStack input5, ItemStack input6,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
        this.input6 = input6;
        this.output = output;
    }

    public static List<GenStarHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenStarHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack input4, ItemStack input5, ItemStack input6, ItemStack output
    ) {
        GenStarHandler recipe = new GenStarHandler(input, input1, input2, input3, input4, input5, input6, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenStarHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenStarHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("wither")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.input.getInputs().get(5).getInputs().get(0),
                    container.input.getInputs().get(6).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }

    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getInput2() { // Получатель входного предмета рецепта.
        return input2;
    }

    public ItemStack getInput3() {
        return input3;
    }

    public ItemStack getInput4() { // Получатель входного предмета рецепта.
        return input4;
    }

    public ItemStack getInput5() { // Получатель входного предмета рецепта.
        return input5;
    }

    public ItemStack getInput6() { // Получатель входного предмета рецепта.
        return input6;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2) || is.isItemEqual(input3) || is.isItemEqual(
                input4);
    }

}
