package com.denfop.integration.jei.rocketassembler;


import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RocketAssemblerHandler implements IJeiVariantRecipe {

    private static final List<RocketAssemblerHandler> recipes = new ArrayList<>();
    private List<List<ItemStack>> inputVariants = new java.util.ArrayList<>();

    private final List<ItemStack> input;
    ItemStack output;

    public RocketAssemblerHandler(
            List<ItemStack> input,
            ItemStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<RocketAssemblerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RocketAssemblerHandler addRecipe(
            List<ItemStack> input, ItemStack output
    ) {
        RocketAssemblerHandler recipe = new RocketAssemblerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rocketassembler")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getStackInputs(),
                    container.getOutput().items.get(0)
            ), container);


        }
    }


    public List<ItemStack> getInput() {
        return input;
    }


    public ItemStack getOutput() {
        return output.copy();
    }




    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new java.util.ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
