package com.denfop.integration.jei.fluidintegrator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidIntegratorHandler {

    private static final List<FluidIntegratorHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public FluidIntegratorHandler(ItemStack input, ItemStack output, FluidStack inputFluid, FluidStack outputFluid) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<FluidIntegratorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static FluidIntegratorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "fluid_integrator");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("fluid_integrator");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            ItemStack output = baseMachineRecipe.getOutput().items.get(0);
            FluidStack inputFluid = baseMachineRecipe.input.getFluid();
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);


            addRecipe(input, output,
                    inputFluid, outputFluid
            );
        }


    }

    private static FluidIntegratorHandler addRecipe(
            ItemStack input,
            ItemStack output,
            FluidStack inputFluid,
            FluidStack outputFluid
    ) {
        FluidIntegratorHandler recipe = new FluidIntegratorHandler(input, output, inputFluid, outputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getInput() {
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
