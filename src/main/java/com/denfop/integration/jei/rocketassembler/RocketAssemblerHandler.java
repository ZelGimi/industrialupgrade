package com.denfop.integration.jei.rocketassembler;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RocketAssemblerHandler {

    private static final List<RocketAssemblerHandler> recipes = new ArrayList<>();
    private final List<ItemStack> input;
    ItemStack output;

    public RocketAssemblerHandler(
            List<ItemStack> input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<RocketAssemblerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RocketAssemblerHandler addRecipe(
            List<ItemStack> input, ItemStack output
    ) {
        RocketAssemblerHandler recipe = new RocketAssemblerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rocketassembler")) {
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
