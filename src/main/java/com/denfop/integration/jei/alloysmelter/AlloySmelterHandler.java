package com.denfop.integration.jei.alloysmelter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlloySmelterHandler {

    private static final List<AlloySmelterHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, output;
    public final short temperature;

    public AlloySmelterHandler(ItemStack input, ItemStack input1, ItemStack output, final short temperature) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.temperature = temperature;
    }

    public static List<AlloySmelterHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AlloySmelterHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, final short temperature) {
        AlloySmelterHandler recipe = new AlloySmelterHandler(input, input1, output, temperature);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (AlloySmelterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("alloysmelter")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().metadata.getShort("temperature")
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
