package com.denfop.integration.jei.replicator;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReplicatorHandler implements IJeiVariantRecipe {

    private static final List<ReplicatorHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
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


            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.getOutput().metadata.getDouble("matter") * 1000,
                    container.input.getInputs().get(0).getInputs().get(0)
            ), container);


        }


    }


    public double getMatter() {
        return input;
    }

    public ItemStack getOutput() {
        return input2;
    }



    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
