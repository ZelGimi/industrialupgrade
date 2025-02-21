package com.denfop.integration.jei.quantummolecular;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumMolecularHandler {

    private static final List<QuantumMolecularHandler> recipes = new ArrayList<>();
    private final double energy;
    private final ItemStack input, input1, output;

    public QuantumMolecularHandler(ItemStack input, ItemStack input1, ItemStack output, double energy) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.energy = energy;
    }

    public static List<QuantumMolecularHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static QuantumMolecularHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            double energy
    ) {
        QuantumMolecularHandler recipe = new QuantumMolecularHandler(input, input1, output, energy);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static QuantumMolecularHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (QuantumMolecularHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("quantummolecular")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().metadata.getDouble("energy")
            );


        }
    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public double getEnergy() {
        return energy;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
