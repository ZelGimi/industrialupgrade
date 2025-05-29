package com.denfop.integration.jei.geneticstabilizer;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GeneticStabilizerHandler {

    private static final List<GeneticStabilizerHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public GeneticStabilizerHandler(ItemStack input, FluidStack inputFluid, FluidStack outputFluid) {
        this.input = input;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<GeneticStabilizerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static GeneticStabilizerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "genetic_stabilizer");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("genetic_stabilizer");
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

    private static GeneticStabilizerHandler addRecipe(ItemStack input, FluidStack inputFluid, FluidStack outputFluid) {
        GeneticStabilizerHandler recipe = new GeneticStabilizerHandler(input, inputFluid, outputFluid);
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
