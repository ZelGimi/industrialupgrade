package com.denfop.integration.jei.radioactiveorehandler;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RadioactiveOreHandlerHandler {

    private static final List<RadioactiveOreHandlerHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final ItemStack input1;
    public final int chance;

    public RadioactiveOreHandlerHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            int chance
    ) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.chance = chance;
    }

    public static List<RadioactiveOreHandlerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RadioactiveOreHandlerHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack output, int chance
    ) {
        RadioactiveOreHandlerHandler recipe = new RadioactiveOreHandlerHandler(input, input1, output, chance);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RadioactiveOreHandlerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RadioactiveOreHandlerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("radioactive_handler")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0), container.getOutput().items.get(1),
                    container.getOutput().items.get(0), container.getOutput().metadata.getInt("random")
            );


        }
    }

    public ItemStack getInput1() {
        return input1;
    }

    public int getChance() {
        return chance;
    }

    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
