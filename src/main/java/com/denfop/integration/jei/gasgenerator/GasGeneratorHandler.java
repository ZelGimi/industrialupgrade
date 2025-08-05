package com.denfop.integration.jei.gasgenerator;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GasGeneratorHandler {

    private static final List<GasGeneratorHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GasGeneratorHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GasGeneratorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasGeneratorHandler addRecipe(
            double input, FluidStack input2
    ) {
        GasGeneratorHandler recipe = new GasGeneratorHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GasGeneratorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GasGeneratorHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(225000, new FluidStack(FluidName.fluidgas.getInstance().get(), 1000));


    }


    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
