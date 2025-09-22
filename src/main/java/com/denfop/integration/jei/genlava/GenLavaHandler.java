package com.denfop.integration.jei.genlava;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenLavaHandler {

    private static final List<GenLavaHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final int input;


    public GenLavaHandler(
            int input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenLavaHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenLavaHandler addRecipe(
            int input, FluidStack input2
    ) {
        GenLavaHandler recipe = new GenLavaHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenLavaHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenLavaHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(80000, new FluidStack(FluidRegistry.LAVA, 1000));


    }


    public int getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
