package com.denfop.integration.jei.geothermal;


import com.denfop.blocks.FluidName;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GeoThermalHandler {

    private static final List<GeoThermalHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public GeoThermalHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<GeoThermalHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GeoThermalHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        GeoThermalHandler recipe = new GeoThermalHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(
                new FluidStack(FluidName.fluidhot_coolant.getInstance().get(), 3),
                new FluidStack(FluidName.fluidneft.getInstance().get(), 1)
        );
    }

    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
