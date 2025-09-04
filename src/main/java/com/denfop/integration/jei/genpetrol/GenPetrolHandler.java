package com.denfop.integration.jei.genpetrol;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenPetrolHandler {

    private static final List<GenPetrolHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenPetrolHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenPetrolHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenPetrolHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenPetrolHandler recipe = new GenPetrolHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenPetrolHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenPetrolHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(50000, new FluidStack(FluidName.fluidgasoline.getInstance().get(), 1000));
        addRecipe(100000, new FluidStack(FluidName.fluidpetrol90.getInstance().get(), 1000));
        addRecipe(200000, new FluidStack(FluidName.fluidpetrol95.getInstance().get(), 1000));
        addRecipe(400000, new FluidStack(FluidName.fluidpetrol100.getInstance().get(), 1000));
        addRecipe(800000, new FluidStack(FluidName.fluidpetrol105.getInstance().get(), 1000));


    }


    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
