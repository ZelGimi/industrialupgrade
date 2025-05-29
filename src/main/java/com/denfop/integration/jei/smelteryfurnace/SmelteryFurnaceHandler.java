package com.denfop.integration.jei.smelteryfurnace;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class SmelteryFurnaceHandler {

    private static final List<SmelteryFurnaceHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack outputFluid;
    private final BaseMachineRecipe container;


    public SmelteryFurnaceHandler(ItemStack input, FluidStack outputFluid, BaseMachineRecipe baseMachineRecipe) {
        this.input = input;
        this.outputFluid = outputFluid;
        this.container  =baseMachineRecipe;
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public static List<SmelteryFurnaceHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static SmelteryFurnaceHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        final List<BaseFluidMachineRecipe> list = Recipes.recipes.getRecipeFluid().getRecipeList(
                "smeltery");
        final List<BaseMachineRecipe> list1 = Recipes.recipes.getRecipeList("smeltery");
        for (int i = 0; i < list1.size(); i++) {
            BaseFluidMachineRecipe baseFluidMachineRecipe = list.get(i);
            BaseMachineRecipe baseMachineRecipe = list1.get(i);
            ItemStack input = ItemStack.EMPTY;
            try {
                input = baseMachineRecipe.input.getInputs().get(0).getInputs().get(0);
            } catch (Exception e) {
                e.printStackTrace();
                ;
            }
            FluidStack outputFluid = baseFluidMachineRecipe.output_fluid.get(0);


            addRecipe(
                    input,
                    outputFluid,baseMachineRecipe
            );
        }


    }

    private static SmelteryFurnaceHandler addRecipe(ItemStack input, FluidStack outputFluid, BaseMachineRecipe baseMachineRecipe) {
        SmelteryFurnaceHandler recipe = new SmelteryFurnaceHandler(input, outputFluid,baseMachineRecipe);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public ItemStack getInput() {
        return input;
    }


    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
