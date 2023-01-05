package com.denfop.integration.jei.plasticcratorplate;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class PlasticCreatorPlateHandler {

    private static final List<PlasticCreatorPlateHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final ItemStack input, output;

    public PlasticCreatorPlateHandler(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
    }

    public static List<PlasticCreatorPlateHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PlasticCreatorPlateHandler addRecipe(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        PlasticCreatorPlateHandler recipe = new PlasticCreatorPlateHandler(input, input2, output);
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

            addRecipe(container.input.getInputs().get(0).getInputs().get(0), container.input.getFluid(),

                    container.getOutput().items.get(0)
            );

        }
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
        return is.isItemEqual(input);
    }

}
