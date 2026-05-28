package com.denfop.integration.jei.itemdivider;


import com.denfop.integration.jei.IJeiVariantRecipe;
import com.denfop.integration.jei.JeiIngredientHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ItemDividerHandler implements IJeiVariantRecipe {

    private static final List<ItemDividerHandler> recipes = new ArrayList<>();
    
    private List<List<ItemStack>> inputVariants = new ArrayList<>();
private final ItemStack input;
    private final ItemStack output;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;


    public ItemDividerHandler(ItemStack input, ItemStack output, FluidStack inputFluid, FluidStack outputFluid) {
        this.input = input;
        this.output = output;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
    }

    public static List<ItemDividerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static ItemDividerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "item_divider");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("item_divider");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            ItemStack output = baseMachineRecipe.getOutput().items.get(0);
            FluidStack inputFluid = baseFluidMachineRecipe.output_fluid.get(0);
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(1);


            JeiIngredientHelper.attachInputVariants(addRecipe(input, output,
                    inputFluid, outputFluid
            ), baseMachineRecipe);
        }


    }

    private static ItemDividerHandler addRecipe(
            ItemStack input,
            ItemStack output,
            FluidStack inputFluid,
            FluidStack outputFluid
    ) {
        ItemDividerHandler recipe = new ItemDividerHandler(input, output, inputFluid, outputFluid);
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



    @Override
    public void setInputVariants(final List<List<ItemStack>> inputVariants) {
        this.inputVariants = inputVariants == null ? new ArrayList<>() : inputVariants;
    }

    @Override
    public List<ItemStack> getInputVariants(final int slot, final ItemStack fallback) {
        return JeiIngredientHelper.getInputVariants(this.inputVariants, slot, fallback);
    }
}
