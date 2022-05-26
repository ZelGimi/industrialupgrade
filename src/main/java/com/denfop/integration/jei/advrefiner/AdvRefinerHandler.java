package com.denfop.integration.jei.advrefiner;


import com.denfop.api.IFluidRecipeManager;
import com.denfop.api.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvRefinerHandler {

    private static final List<AdvRefinerHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output;

    public AdvRefinerHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<AdvRefinerHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AdvRefinerHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        AdvRefinerHandler recipe = new AdvRefinerHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AdvRefinerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        for (Map.Entry<IFluidRecipeManager.Input, FluidStack[]> container :
                Recipes.oiladvrefiner.getRecipes().entrySet()) {
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

    public FluidStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return output;
    }

    public FluidStack getOutput1() { // Получатель выходного предмета рецепта.
        return output1;
    }

}
