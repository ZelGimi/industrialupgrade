package com.denfop.integration.jei.gas_turbine;



import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.denfop.tiles.gasturbine.TileEntityGasTurbineController.gasMapValue;

public class GasTurbineHandler {

    private static final List<GasTurbineHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GasTurbineHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;

    }

    public static List<GasTurbineHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasTurbineHandler addRecipe(
            double input, FluidStack input2
    ) {
        GasTurbineHandler recipe = new GasTurbineHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GasTurbineHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GasTurbineHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<Fluid, Integer> integerMap : gasMapValue.entrySet()) {
            addRecipe(integerMap.getValue() * 1000, new FluidStack(integerMap.getKey(), 1000));
        }


    }


    public double getEnergy() {
        return input;
    }

    public FluidStack getOutput() {
        return input2;
    }

}
