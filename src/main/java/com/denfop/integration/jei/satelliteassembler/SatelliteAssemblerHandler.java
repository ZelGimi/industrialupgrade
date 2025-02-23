package com.denfop.integration.jei.satelliteassembler;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SatelliteAssemblerHandler {

    private static final List<SatelliteAssemblerHandler> recipes = new ArrayList<>();
    private final List<ItemStack> input;
    ItemStack output;

    public SatelliteAssemblerHandler(
            List<ItemStack> input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<SatelliteAssemblerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SatelliteAssemblerHandler addRecipe(
            List<ItemStack> input, ItemStack output
    ) {
        SatelliteAssemblerHandler recipe = new SatelliteAssemblerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }



    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("satelliteassembler")) {
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
