package com.denfop.integration.jei.molecular;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MolecularTransformerHandler {

    private static final List<MolecularTransformerHandler> recipes = new ArrayList<>();
    private final double energy;
    private final ItemStack input, output;

    public MolecularTransformerHandler(ItemStack input, ItemStack output, double energy) {
        this.input = input;
        this.output = output;
        this.energy = energy;
    }

    public static List<MolecularTransformerHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static MolecularTransformerHandler addRecipe(ItemStack input, ItemStack output, double energy) {
        MolecularTransformerHandler recipe = new MolecularTransformerHandler(input, output, energy);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static MolecularTransformerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (MolecularTransformerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("molecular")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getDouble("energy")
            );


        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public double getEnergy() { // Получатель выходного предмета рецепта.
        return energy;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

}
