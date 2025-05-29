package com.denfop.integration.jei.impalloysmelter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImpAlloySmelterHandler {

    public static final List<ImpAlloySmelterHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, input2, input3, output;
    public final short temperature;
    private final BaseMachineRecipe container;

    public ImpAlloySmelterHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack input3,
            ItemStack output,
            final short temperature,
            BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.output = output;
        this.temperature = temperature;
        this.container=container;
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public static List<ImpAlloySmelterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ImpAlloySmelterHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack input3,
            ItemStack output,
            final short temperature,
            BaseMachineRecipe container) {
        ImpAlloySmelterHandler recipe = new ImpAlloySmelterHandler(input, input1, input2, input3, output, temperature,container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ImpAlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ImpAlloySmelterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("impalloysmelter")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0), container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getShort("temperature"),container
            );


        }
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1, input3);
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

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
