package com.denfop.integration.jei.fluidsolidmixer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidSolidMixerHandler {

    private static final List<FluidSolidMixerHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack outputFluid1;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public FluidSolidMixerHandler(ItemStack input, FluidStack inputFluid, FluidStack outputFluid, FluidStack outputFluid1) {
        this.input = input;
        this.outputFluid1 = outputFluid1;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<FluidSolidMixerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static FluidSolidMixerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "solid_fluid_mixer");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("solid_fluid_mixer");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            FluidStack inputFluid = baseMachineRecipe.input.getFluid();
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);
            FluidStack outputFluid1 = baseFluidMachineRecipe.output_fluid.get(1);


            addRecipe(input,
                    inputFluid, outputFluid, outputFluid1
            );
        }


    }

    private static FluidSolidMixerHandler addRecipe(
            ItemStack input, FluidStack inputFluid, FluidStack outputFluid,
            FluidStack outputFluid1
    ) {
        FluidSolidMixerHandler recipe = new FluidSolidMixerHandler(input, inputFluid, outputFluid, outputFluid1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutputFluid1() {
        return outputFluid1;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
