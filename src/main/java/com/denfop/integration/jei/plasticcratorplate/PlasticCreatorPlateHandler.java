package com.denfop.integration.jei.plasticcratorplate;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class PlasticCreatorPlateHandler implements IJeiVariantRecipe {

    private static final List<PlasticCreatorPlateHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final FluidStack input2;
    private final ItemStack input, output;
    private final BaseMachineRecipe container;

    public PlasticCreatorPlateHandler(
            ItemStack input, FluidStack input2,
            ItemStack output,
            BaseMachineRecipe container) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
        this.container = container;
    }

    public static List<PlasticCreatorPlateHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PlasticCreatorPlateHandler addRecipe(
            ItemStack input, FluidStack input2,
            ItemStack output,
            BaseMachineRecipe container) {
        PlasticCreatorPlateHandler recipe = new PlasticCreatorPlateHandler(input, input2, output, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PlasticCreatorPlateHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PlasticCreatorPlateHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("plasticplate")) {

            JeiIngredientHelper.attachInputVariants(addRecipe(container.input.getInputs().get(0).getInputs().get(0), container.input.getFluid(),

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

    public FluidStack getInput2() { // Получатель входного предмета рецепта.
        return input2;
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
