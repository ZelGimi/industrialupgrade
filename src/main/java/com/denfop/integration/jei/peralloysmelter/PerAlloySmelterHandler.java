package com.denfop.integration.jei.peralloysmelter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PerAlloySmelterHandler {

    public static final List<PerAlloySmelterHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, input2, input3, input4, output;
    public final short temperature;

    public PerAlloySmelterHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack input3,
            ItemStack input4,
            ItemStack output,
            final short temperature
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.output = output;
        this.temperature = temperature;
    }

    public static List<PerAlloySmelterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PerAlloySmelterHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack input3,
            ItemStack input4,
            ItemStack output,
            final short temperature
    ) {
        PerAlloySmelterHandler recipe = new PerAlloySmelterHandler(input, input1, input2, input3, input4, output, temperature);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PerAlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PerAlloySmelterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("peralloysmelter")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0), container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getShort("temperature")
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
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2);
    }

}
