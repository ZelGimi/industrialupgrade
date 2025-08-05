package com.denfop.integration.jei.generator;


import com.denfop.componets.Fluids;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GeneratorHandler {

    private static final List<GeneratorHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GeneratorHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GeneratorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GeneratorHandler addRecipe(
            double input, FluidStack input2
    ) {
        GeneratorHandler recipe = new GeneratorHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GeneratorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GeneratorHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(10000, new FluidStack(Fluids.LAVA, 1000));


    }


    public double getEnergy() {
        return input;
    }

    public FluidStack getOutput() {
        return input2;
    }

}
