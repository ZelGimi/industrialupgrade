package com.denfop.integration.jei.worldcollector.crystallize;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrystallizeHandler implements IJeiVariantRecipe {

    private static final List<CrystallizeHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, output;
    private final double need;

    public CrystallizeHandler(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        this.input = input;
        this.output = output;
        this.need = need;
    }

    public static List<CrystallizeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CrystallizeHandler addRecipe(
            ItemStack input,
            ItemStack output,
            double need
    ) {
        CrystallizeHandler recipe = new CrystallizeHandler(input, output, need);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CrystallizeHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (CrystallizeHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("defaultcollector")) {

            JeiIngredientHelper.attachInputVariants(addRecipe(container.input.getInputs().get(0).getInputs().get(0),

                    container.getOutput().items.get(0), container.getOutput().metadata.getDouble("need")
            ), container);

        }
    }

    public double getNeed() {
        return need;
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
