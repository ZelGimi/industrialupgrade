package com.denfop.integration.jei.sunnariumpanel;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SannariumPanelHandler implements IJeiVariantRecipe {

    private static final List<SannariumPanelHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, input1, output;
    private final BaseMachineRecipe container;

    public SannariumPanelHandler(ItemStack input, ItemStack input1, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.container = container;
    }

    public static List<SannariumPanelHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static SannariumPanelHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, BaseMachineRecipe container) {
        SannariumPanelHandler recipe = new SannariumPanelHandler(input, input1, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static SannariumPanelHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (SannariumPanelHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("sunnuriumpanel")) {
            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0), container
            ), container);


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
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
