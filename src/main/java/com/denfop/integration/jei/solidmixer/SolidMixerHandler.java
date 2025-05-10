package com.denfop.integration.jei.solidmixer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolidMixerHandler {

    private static final List<SolidMixerHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, output, output1;

    public SolidMixerHandler(
            ItemStack input, ItemStack input1,
            ItemStack output,
            ItemStack output1
    ) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.output1 = output1;
    }

    public static List<SolidMixerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SolidMixerHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack output, ItemStack output1
    ) {
        SolidMixerHandler recipe = new SolidMixerHandler(input, input1, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SolidMixerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SolidMixerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("solid_mixer")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().items.get(1)
            );


        }
    }

    public ItemStack getOutput1() {
        return output1;
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

    public boolean matchesInput(ItemStack is) {
        return true;
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1);
    }
    public List<ItemStack> getOutputs() {
        return Arrays.asList(output, output1);
    }
}
