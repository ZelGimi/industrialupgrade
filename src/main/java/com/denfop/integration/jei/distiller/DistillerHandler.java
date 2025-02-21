package com.denfop.integration.jei.distiller;


import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class DistillerHandler {

    private static final List<DistillerHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public DistillerHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<DistillerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static DistillerHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        DistillerHandler recipe = new DistillerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(
                new FluidStack(FluidRegistry.WATER, 4000),
                new FluidStack(FluidName.fluiddistilled_water.getInstance(), 1000)
        );
    }

    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
