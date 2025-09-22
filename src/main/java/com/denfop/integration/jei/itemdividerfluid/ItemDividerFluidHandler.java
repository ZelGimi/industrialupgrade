package com.denfop.integration.jei.itemdividerfluid;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ItemDividerFluidHandler {

    private static final List<ItemDividerFluidHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack output1, output;

    public ItemDividerFluidHandler(
            ItemStack input, FluidStack output,
            FluidStack output1
    ) {
        this.input = input;
        this.output1 = output1;
        this.output = output;
    }

    public static List<ItemDividerFluidHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ItemDividerFluidHandler addRecipe(
            ItemStack input, FluidStack output,
            FluidStack output1
    ) {
        ItemDividerFluidHandler recipe = new ItemDividerFluidHandler(input, output, output1);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ItemDividerFluidHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        for (BaseFluidMachineRecipe machineRecipe : Recipes.recipes.getRecipeFluid().getRecipeList(
                "item_divider_fluid")) {
            ItemStack itemStack = machineRecipe.getInput().getStack().getInputs().get(0);
            FluidStack fluidStack1 = machineRecipe.getOutput_fluid().get(0);
            FluidStack fluidStack2 = machineRecipe.getOutput_fluid().get(1);

            addRecipe(itemStack, fluidStack1,
                    fluidStack2
            );
        }


    }


    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }

    public FluidStack getOutput1() {
        return output1;
    }

}
