package com.denfop.integration.jei.orewashing;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OreWashingHandler implements IJeiVariantRecipe {

    private static final List<OreWashingHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input;
    private final List<ItemStack> output;
    private final short temperature;
    private final BaseMachineRecipe container;

    public OreWashingHandler(ItemStack input, List<ItemStack> output, short temperature, BaseMachineRecipe container) {
        this.input = input;
        this.output = output;
        this.temperature = temperature;
        this.container = container;
    }

    public static List<OreWashingHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static OreWashingHandler addRecipe(ItemStack input, List<ItemStack> output, short temperature, BaseMachineRecipe container) {
        OreWashingHandler recipe = new OreWashingHandler(input, output, temperature, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static OreWashingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (OreWashingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("orewashing")) {


            JeiIngredientHelper.attachInputVariants(addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items, (short) 1000, container
            ), container);


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public List<ItemStack> getOutput() { // Получатель выходного предмета рецепта.
        return output;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

    public short getTemperature() {
        return this.temperature;
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
