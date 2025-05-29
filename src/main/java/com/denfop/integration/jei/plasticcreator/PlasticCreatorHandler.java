package com.denfop.integration.jei.plasticcreator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class PlasticCreatorHandler {

    private static final List<PlasticCreatorHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final ItemStack input, input1, output;
    private final BaseMachineRecipe container;

    public PlasticCreatorHandler(
            ItemStack input, ItemStack input1, FluidStack input2,
            ItemStack output,
            BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.container = container;
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public static List<PlasticCreatorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PlasticCreatorHandler addRecipe(
            ItemStack input, ItemStack input1, FluidStack input2,
            ItemStack output,
            BaseMachineRecipe container) {
        PlasticCreatorHandler recipe = new PlasticCreatorHandler(input, input1, input2, output,container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PlasticCreatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PlasticCreatorHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("plastic")) {
        try {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getFluid(),

                    container.getOutput().items.get(0), container
            );
        }catch (Exception e){
            System.out.println(2);
        }
        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
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

}
