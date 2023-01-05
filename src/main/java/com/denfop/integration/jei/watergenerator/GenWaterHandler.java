package com.denfop.integration.jei.watergenerator;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenWaterHandler {

    private static final List<GenWaterHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final int input;


    public GenWaterHandler(
            int input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenWaterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenWaterHandler addRecipe(
            int input, FluidStack input2
    ) {
        GenWaterHandler recipe = new GenWaterHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenWaterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenWaterHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(40000, new FluidStack(FluidRegistry.WATER, 1000));
    }


    public int getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
