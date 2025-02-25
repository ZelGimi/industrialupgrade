package com.denfop.integration.jei.genbio;


import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenBioHandler {

    private static final List<GenBioHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenBioHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenBioHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenBioHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenBioHandler recipe = new GenBioHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenBioHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenBioHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(5000, new FluidStack(FluidName.fluidbiomass.getInstance(), 1000));


    }


    public double getEnergy() {
        return input;
    }

    public FluidStack getOutput() {
        return input2;
    }

}
