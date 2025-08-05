package com.denfop.integration.jei.genhydrogen;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenHydHandler {

    private static final List<GenHydHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenHydHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenHydHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenHydHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenHydHandler recipe = new GenHydHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenHydHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenHydHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(10000, new FluidStack(FluidName.fluidhyd.getInstance().get(), 1000));


    }


    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
