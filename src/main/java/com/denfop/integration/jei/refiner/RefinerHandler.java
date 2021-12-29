package com.denfop.integration.jei.refiner;


import com.denfop.api.IFluidRecipeManager;
import com.denfop.api.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefinerHandler {

    private static final List<RefinerHandler> recipes = new ArrayList<>();
    private final FluidStack output1;


    public static List<RefinerHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    private final FluidStack input, output;


    public RefinerHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }


    public FluidStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return output;
    }

    public FluidStack getOutput1() { // Получатель выходного предмета рецепта.
        return output1;
    }

    public static RefinerHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        RefinerHandler recipe = new RefinerHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RefinerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }


    public static void initRecipes() {
        for (Map.Entry<IFluidRecipeManager.Input, FluidStack[]> container :
                Recipes.oilrefiner.getRecipes().entrySet()) {
            addRecipe(container.getKey().fluidStack, container.getValue()[0],

                    container.getValue()[1]
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
