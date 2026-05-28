package com.denfop.integration.jei.anvil;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AnvilHandler implements IJeiVariantRecipe {

    private static final List<AnvilHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input;
    private final ItemStack output;
    private final BaseMachineRecipe container;


    public AnvilHandler(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.container = container;
    }

    public static List<AnvilHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AnvilHandler addRecipe(
            ItemStack input, ItemStack output,
            BaseMachineRecipe container) {
        AnvilHandler recipe = new AnvilHandler(input, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("anvil")) {
            try {
                JeiIngredientHelper.attachInputVariants(addRecipe(
                        container.input.getInputs().get(0).getInputs().get(0),
                        container.getOutput().items.get(0), container
                ), container);
            } catch (Exception e) {
                System.out.println(2);
            }
            ;


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
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
