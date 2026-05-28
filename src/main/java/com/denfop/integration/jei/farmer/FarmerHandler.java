package com.denfop.integration.jei.farmer;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FarmerHandler implements IJeiVariantRecipe {

    private static final List<FarmerHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, output;

    public FarmerHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<FarmerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FarmerHandler addRecipe(ItemStack input, ItemStack output) {
        FarmerHandler recipe = new FarmerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static FarmerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (FarmerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("farmer")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items.get(0)
            ), container);


        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
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
