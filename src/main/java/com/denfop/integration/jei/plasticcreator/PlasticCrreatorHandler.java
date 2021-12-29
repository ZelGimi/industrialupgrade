package com.denfop.integration.jei.plasticcreator;


import com.denfop.api.IPlasticRecipemanager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlasticCrreatorHandler {

    private static final List<PlasticCrreatorHandler> recipes = new ArrayList<>();
    private final FluidStack input2;


    public static List<PlasticCrreatorHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    private final ItemStack input, input1, output;


    public PlasticCrreatorHandler(
            ItemStack input, ItemStack input1, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
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

    public static PlasticCrreatorHandler addRecipe(
            ItemStack input, ItemStack input1, FluidStack input2,
            ItemStack output
    ) {
        PlasticCrreatorHandler recipe = new PlasticCrreatorHandler(input, input1, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PlasticCrreatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PlasticCrreatorHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

    public static void initRecipes() {
        for (Map.Entry<IPlasticRecipemanager.Input, RecipeOutput> container :
                Recipes.plastic.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0), container.getKey().fill.getInputs().get(0),
                    container.getKey().fluidStack,

                    container.getValue().items.get(0)
            );

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

}
