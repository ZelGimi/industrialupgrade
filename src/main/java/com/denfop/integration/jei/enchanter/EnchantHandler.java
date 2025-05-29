package com.denfop.integration.jei.enchanter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantHandler {

    private static final List<EnchantHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, output;

    public EnchantHandler(
            ItemStack input, ItemStack input1,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
    }

    public static List<EnchantHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EnchantHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack output
    ) {
        EnchantHandler recipe = new EnchantHandler(input, input1, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static EnchantHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (EnchantHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("enchanter_books")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0)
            );


        }
    }


    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1);
    }
    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
