package com.denfop.integration.jei.fluidintegrator;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidIntegratorHandler implements IJeiVariantRecipe {

    private static final List<FluidIntegratorHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;
    private final BaseMachineRecipe container;


    public FluidIntegratorHandler(ItemStack input, ItemStack output, FluidStack inputFluid, FluidStack outputFluid, BaseMachineRecipe baseMachineRecipe) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.container = baseMachineRecipe;
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


            JeiIngredientHelper.attachInputVariants(addRecipe(input, output,
                    inputFluid, outputFluid, baseMachineRecipe
            ), baseMachineRecipe);
        }


    }

    private static FluidIntegratorHandler addRecipe(
            ItemStack input,
            ItemStack output,
            FluidStack inputFluid,
            FluidStack outputFluid,
            BaseMachineRecipe baseMachineRecipe) {
        FluidIntegratorHandler recipe = new FluidIntegratorHandler(input, output, inputFluid, outputFluid, baseMachineRecipe);
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



    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
