package com.denfop.integration.jei.replicator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReplicatorHandler {

    private static final List<ReplicatorHandler> recipes = new ArrayList<>();
    private final ItemStack input2;
    private final double input;


    public ReplicatorHandler(
            double input, ItemStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<ReplicatorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ReplicatorHandler addRecipe(
            double input, ItemStack input2
    ) {
        ReplicatorHandler recipe = new ReplicatorHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ReplicatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ReplicatorHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("replicator")) {


            addRecipe(
                    container.getOutput().metadata.getDouble("matter") * 1000,
                    container.input.getInputs().get(0).getInputs().get(0)
            );


        }


    }


    public double getMatter() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }

}
