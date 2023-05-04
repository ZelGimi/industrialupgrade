package com.denfop.integration.jei.canningsolid;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CanningSolidHandler {

    private static final List<CanningSolidHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, output;


    public CanningSolidHandler(ItemStack input, ItemStack input1, ItemStack output) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
    }

    public static List<CanningSolidHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CanningSolidHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output) {
        CanningSolidHandler recipe = new CanningSolidHandler(input, input1, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CanningSolidHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CanningSolidHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("cannerbottle")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
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

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
