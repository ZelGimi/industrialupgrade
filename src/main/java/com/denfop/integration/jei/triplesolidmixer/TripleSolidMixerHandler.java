package com.denfop.integration.jei.triplesolidmixer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TripleSolidMixerHandler {

    private static final List<TripleSolidMixerHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, input2, output, output1;

    public TripleSolidMixerHandler(
            ItemStack input, ItemStack input1, ItemStack input2,
            ItemStack output,
            ItemStack output1
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.output1 = output1;
    }

    public static List<TripleSolidMixerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static TripleSolidMixerHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack output, ItemStack output1
    ) {
        TripleSolidMixerHandler recipe = new TripleSolidMixerHandler(input, input1, input2, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static TripleSolidMixerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (TripleSolidMixerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("triple_solid_mixer")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0), container.input.getInputs().get(2).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().items.get(1)
            );


        }
    }

    public ItemStack getInput2() {
        return input2;
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
        return is.isItemEqual(input);
    }

}
