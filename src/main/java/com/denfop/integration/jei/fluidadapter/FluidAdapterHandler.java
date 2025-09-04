package com.denfop.integration.jei.fluidadapter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidAdapterHandler {

    private static final List<FluidAdapterHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;
    private final BaseMachineRecipe container;


    public FluidAdapterHandler(ItemStack input, ItemStack output, FluidStack inputFluid, FluidStack outputFluid, BaseMachineRecipe baseMachineRecipe) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.container = baseMachineRecipe;
    }

    public static List<FluidAdapterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static FluidAdapterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "fluid_adapter");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("fluid_adapter");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            ItemStack input1 = baseMachineRecipe.input.getInputs().get(1).getInputs().get(0);
            FluidStack inputFluid = baseMachineRecipe.input.getFluid();
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);


            addRecipe(input, input1,
                    inputFluid, outputFluid, baseMachineRecipe
            );
        }


    }

    private static FluidAdapterHandler addRecipe(
            ItemStack input,
            ItemStack output,
            FluidStack inputFluid,
            FluidStack outputFluid,
            BaseMachineRecipe baseMachineRecipe) {
        FluidAdapterHandler recipe = new FluidAdapterHandler(input, output, inputFluid, outputFluid, baseMachineRecipe);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public BaseMachineRecipe getContainer() {
        return container;
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
