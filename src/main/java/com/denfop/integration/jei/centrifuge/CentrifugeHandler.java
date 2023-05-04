package com.denfop.integration.jei.centrifuge;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CentrifugeHandler {

    private static final List<CentrifugeHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final List<ItemStack> output;
    private final short temperature;

    public CentrifugeHandler(ItemStack input, List<ItemStack> output, short temperature) {
        this.input = input;
        this.output = output;
        this.temperature = temperature;
    }

    public static List<CentrifugeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CentrifugeHandler addRecipe(ItemStack input, List<ItemStack> output, short temperature) {
        CentrifugeHandler recipe = new CentrifugeHandler(input, output, temperature);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CentrifugeHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CentrifugeHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("centrifuge")) {


            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items, container.getOutput().metadata.getShort("minHeat")
            );


        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public List<ItemStack> getOutput() { // Получатель выходного предмета рецепта.
        return output;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

    public short getTemperature() {
        return this.temperature;
    }

}
