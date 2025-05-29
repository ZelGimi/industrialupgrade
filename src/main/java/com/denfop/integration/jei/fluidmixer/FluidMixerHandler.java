package com.denfop.integration.jei.fluidmixer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidMixerHandler {

    private static final List<FluidMixerHandler> recipes = new ArrayList<>();
    private final FluidStack output1;
    private final FluidStack input, output, output2;

    public FluidMixerHandler(
            FluidStack input, FluidStack output,
            FluidStack output1, FluidStack output2
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
        this.output2 = output2;
    }

    public static List<FluidMixerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FluidMixerHandler addRecipe(
            FluidStack input, FluidStack output,
            FluidStack output1,
            FluidStack output2
    ) {
        FluidMixerHandler recipe = new FluidMixerHandler(input, output, output1, output2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static FluidMixerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        for (BaseFluidMachineRecipe machineRecipe : Recipes.recipes.getRecipeFluid().getRecipeList(
                "fluid_mixer")) {
            FluidStack fluidStack = machineRecipe.getInput().getInputs().get(0);
            FluidStack fluidStack1 = machineRecipe.getInput().getInputs().get(1);
            FluidStack fluidStack2 = machineRecipe.getOutput_fluid().get(0);
            FluidStack fluidStack3 = machineRecipe.getOutput_fluid().get(1);

            addRecipe(fluidStack, fluidStack1,
                    fluidStack2,
                    fluidStack3
            );
        }


    }

    public FluidStack getOutput2() {
        return output2;
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
