package com.denfop.integration.jei.plasticcratorplate;


import com.denfop.api.IPlasticPlateRecipemanager;
import com.denfop.api.Recipes;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlasticCreatorPlateHandler {
    private static List<PlasticCreatorPlateHandler> recipes = new ArrayList<>();
    private final FluidStack input2;


    public static List<PlasticCreatorPlateHandler> getRecipes() { // Получатель всех рецептов.
        if(recipes.isEmpty())
            initRecipes();
        return recipes;
    }

    private final ItemStack input, output;


    public PlasticCreatorPlateHandler(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
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

    public static PlasticCreatorPlateHandler addRecipe(
            ItemStack input, FluidStack input2,
            ItemStack output
    ) {
        PlasticCreatorPlateHandler recipe = new PlasticCreatorPlateHandler(input, input2, output);
        if (recipes.contains(recipe))
            return null;
        recipes.add(recipe);
        return recipe;
    }

    public static PlasticCreatorPlateHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty())
            return null;
        for (PlasticCreatorPlateHandler recipe : recipes)
            if (recipe.matchesInput(is))
                return recipe;
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) ;
    }

    public static void initRecipes() {
        for (Map.Entry<IPlasticPlateRecipemanager.Input, RecipeOutput> container :
                Recipes.plasticplate.getRecipes().entrySet()) {
            addRecipe(container.getKey().container.getInputs().get(0),container.getKey().fluidStack,

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
