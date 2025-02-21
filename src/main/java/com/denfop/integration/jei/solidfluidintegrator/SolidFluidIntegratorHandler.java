package com.denfop.integration.jei.solidfluidintegrator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SolidFluidIntegratorHandler {

    private static final List<SolidFluidIntegratorHandler> recipes = new ArrayList<>();
    private final FluidStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public SolidFluidIntegratorHandler(FluidStack input, ItemStack output, FluidStack inputFluid, FluidStack outputFluid) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<SolidFluidIntegratorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static SolidFluidIntegratorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "solid_fluid_integrator");
        for (BaseFluidMachineRecipe baseFluidMachineRecipe : list) {
            FluidStack input = baseFluidMachineRecipe.input.getInputs().get(0);
            ItemStack output = baseFluidMachineRecipe.getOutput().items.get(0);
            FluidStack inputFluid = baseFluidMachineRecipe.input.getInputs().get(1);
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);


            addRecipe(input, output,
                    inputFluid, outputFluid
            );
        }


    }

    private static SolidFluidIntegratorHandler addRecipe(
            FluidStack input,
            ItemStack output,
            FluidStack inputFluid,
            FluidStack outputFluid
    ) {
        SolidFluidIntegratorHandler recipe = new SolidFluidIntegratorHandler(input, output, inputFluid, outputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public FluidStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
