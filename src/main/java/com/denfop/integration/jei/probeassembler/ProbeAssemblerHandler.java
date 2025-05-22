package com.denfop.integration.jei.probeassembler;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProbeAssemblerHandler {

    private static final List<ProbeAssemblerHandler> recipes = new ArrayList<>();
    private final List<ItemStack> input;
    ItemStack output;

    public ProbeAssemblerHandler(
            List<ItemStack> input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<ProbeAssemblerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ProbeAssemblerHandler addRecipe(
            List<ItemStack> input, ItemStack output
    ) {
        ProbeAssemblerHandler recipe = new ProbeAssemblerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("probeassembler")) {
            addRecipe(
                    container.input.getStackInputs(),
                    container.getOutput().items.get(0)
            );


        }
    }


    public List<ItemStack> getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }


}
