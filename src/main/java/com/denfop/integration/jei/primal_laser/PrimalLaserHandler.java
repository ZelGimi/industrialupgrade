package com.denfop.integration.jei.primal_laser;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrimalLaserHandler implements IJeiVariantRecipe {

    private static final List<PrimalLaserHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, output;

    public PrimalLaserHandler(
            ItemStack input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<PrimalLaserHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PrimalLaserHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        PrimalLaserHandler recipe = new PrimalLaserHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PrimalLaserHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PrimalLaserHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("primal_laser_polisher")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            ), container);


        }
    }


    public ItemStack getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
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
