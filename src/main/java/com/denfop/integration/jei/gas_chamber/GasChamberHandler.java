package com.denfop.integration.jei.gas_chamber;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GasChamberHandler {

    private static final List<GasChamberHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output;

    public GasChamberHandler(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<GasChamberHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasChamberHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1
    ) {
        GasChamberHandler recipe = new GasChamberHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GasChamberHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        for (BaseFluidMachineRecipe machineRecipe : Recipes.recipes.getRecipeFluid().getRecipeList(
                "gas_chamber")) {
            FluidStack fluidStack = machineRecipe.getInput().getInputs().get(0);
            FluidStack fluidStack1 = machineRecipe.getInput().getInputs().get(1);
            FluidStack fluidStack2 = machineRecipe.getOutput_fluid().get(0);

            addRecipe(fluidStack, fluidStack1,
                    fluidStack2
            );
        }


    }


    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }

    public FluidStack getOutput1() {
        return output1;
    }

}
