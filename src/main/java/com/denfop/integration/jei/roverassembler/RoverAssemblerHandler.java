package com.denfop.integration.jei.roverassembler;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RoverAssemblerHandler {

    private static final List<RoverAssemblerHandler> recipes = new ArrayList<>();
    private final List<ItemStack> input;
    ItemStack output;

    public RoverAssemblerHandler(
            List<ItemStack> input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<RoverAssemblerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RoverAssemblerHandler addRecipe(
            List<ItemStack> input, ItemStack output
    ) {
        RoverAssemblerHandler recipe = new RoverAssemblerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("roverassembler")) {
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
