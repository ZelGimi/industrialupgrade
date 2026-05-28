package com.denfop.integration.jei.extruder;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExtruderHandler implements IJeiVariantRecipe {

    private static final List<ExtruderHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input, output;
    private final BaseMachineRecipe container;

    public ExtruderHandler(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.container = container;
    }

    public static List<ExtruderHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ExtruderHandler addRecipe(ItemStack input, ItemStack output, BaseMachineRecipe container) {
        ExtruderHandler recipe = new ExtruderHandler(input, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ExtruderHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ExtruderHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("extruding")) {

            try {
                JeiIngredientHelper.attachInputVariants(addRecipe(
                        container.input.getInputs().get(0).getInputs().get(0),
                        container.getOutput().items.get(0), container
                ), container);

            } catch (Exception ignored) {
                System.out.println(2);
            }
        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
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
