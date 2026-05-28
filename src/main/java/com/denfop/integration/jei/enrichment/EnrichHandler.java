package com.denfop.integration.jei.enrichment;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnrichHandler implements IJeiVariantRecipe {

    private static final List<EnrichHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, input1, output;
    private final int radAmount;
    private final BaseMachineRecipe container;

    public EnrichHandler(ItemStack input, ItemStack input1, ItemStack output, final int radAmount, BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.radAmount = radAmount;
        this.container = container;
    }

    public static List<EnrichHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EnrichHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, final int radAmount, BaseMachineRecipe container) {
        EnrichHandler recipe = new EnrichHandler(input, input1, output, radAmount, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static EnrichHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (EnrichHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("enrichment")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0), container.output.metadata.getInt("rad_amount"),
                    container
            ), container);


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public int getRadAmount() {
        return radAmount;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
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
