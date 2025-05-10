package com.denfop.integration.jei.electronics;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElectronicsHandler {

    private static final List<ElectronicsHandler> recipes = new ArrayList<>();

    private final ItemStack input, input1, input2, input3, input4, output;
    private final BaseMachineRecipe container;

    public ElectronicsHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4,
            ItemStack output,
            BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.output = output;
        this.container = container;
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public static List<ElectronicsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ElectronicsHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack input4, ItemStack output,
            BaseMachineRecipe container) {
        ElectronicsHandler recipe = new ElectronicsHandler(input, input1, input2, input3, input4, output,container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ElectronicsHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ElectronicsHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("electronics")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.getOutput().items.get(0),container
            );


        }
    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getInput2() {
        return input2;
    }

    public ItemStack getInput3() {
        return input3;
    }

    public ItemStack getInput4() {
        return input4;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1, input2, input3, input4);
    }
}
