package com.denfop.integration.jei.smeltery_controller;


import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.denfop.blockentity.smeltery.BlockEntitySmelteryController.mapRecipes;

public class SmelteryControllerHandler {

    private static final List<SmelteryControllerHandler> recipes = new ArrayList<>();
    private final List<FluidStack> input;
    private final FluidStack outputFluid;


    public SmelteryControllerHandler(List<FluidStack> input, FluidStack outputFluid) {
        this.input = input;
        this.outputFluid = outputFluid;
    }

    public static List<SmelteryControllerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }


    public static SmelteryControllerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {
        for (Map.Entry<List<FluidStack>, FluidStack> mapRecipes : mapRecipes.entrySet()) {
            addRecipe(
                    mapRecipes.getKey(),
                    mapRecipes.getValue()
            );
        }


    }

    private static SmelteryControllerHandler addRecipe(List<FluidStack> input, FluidStack outputFluid) {
        SmelteryControllerHandler recipe = new SmelteryControllerHandler(input, outputFluid);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public List<FluidStack> getInput() {
        return input;
    }


    public FluidStack getOutputFluid() {
        return outputFluid;
    }

}
