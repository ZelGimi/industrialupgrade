package com.denfop.integration.jei.industrialorepurifier;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IndustrialOrePurifierHandler {

    private static final List<IndustrialOrePurifierHandler> recipes = new ArrayList<>();
    private final ItemStack input, output;
    private final int chance;

    public IndustrialOrePurifierHandler(
            ItemStack input,
            ItemStack output,
            int chance
    ) {
        this.input = input;
        this.output = output;
        this.chance = chance;
    }

    public static List<IndustrialOrePurifierHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static IndustrialOrePurifierHandler addRecipe(
            ItemStack input, ItemStack output, int chance
    ) {
        IndustrialOrePurifierHandler recipe = new IndustrialOrePurifierHandler(input, output, chance);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static IndustrialOrePurifierHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (IndustrialOrePurifierHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("ore_purifier")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getInteger("se")
            );


        }
    }

    public int getChance() {
        return chance;
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
