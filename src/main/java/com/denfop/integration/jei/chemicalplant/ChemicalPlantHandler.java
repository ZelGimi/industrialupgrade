package com.denfop.integration.jei.chemicalplant;


import com.denfop.blocks.FluidName;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ChemicalPlantHandler {

    private static final List<ChemicalPlantHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final FluidStack output;

    public ChemicalPlantHandler(
            FluidStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<ChemicalPlantHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ChemicalPlantHandler addRecipe(
            FluidStack input,
            FluidStack output
    ) {
        ChemicalPlantHandler recipe = new ChemicalPlantHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(
                new FluidStack(FluidName.fluidhelium.getInstance().get(), 2),
                new FluidStack(FluidName.fluidcryogen.getInstance().get(), 10)
        );
    }

    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
