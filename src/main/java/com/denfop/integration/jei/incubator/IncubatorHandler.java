package com.denfop.integration.jei.incubator;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class IncubatorHandler {

    private static final List<IncubatorHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public IncubatorHandler(ItemStack input, FluidStack inputFluid, FluidStack outputFluid) {
        this.input = input;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<IncubatorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static IncubatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "incubator");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("incubator");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            FluidStack inputFluid = baseMachineRecipe.input.getFluid();
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);


            addRecipe(input,
                    inputFluid, outputFluid
            );
        }


    }

    private static IncubatorHandler addRecipe(ItemStack input, FluidStack inputFluid, FluidStack outputFluid) {
        IncubatorHandler recipe = new IncubatorHandler(input, inputFluid, outputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getInput() {
        return input;
    }


    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
