package com.denfop.integration.jei.gendiesel;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenDieselHandler {

    private static final List<GenDieselHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenDieselHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenDieselHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenDieselHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenDieselHandler recipe = new GenDieselHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenDieselHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenDieselHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(1060000, new FluidStack(FluidName.fluiddizel.getInstance().get(), 1000));
        addRecipe(200000, new FluidStack(FluidName.fluida_diesel.getInstance().get(), 1000));
        addRecipe(400000, new FluidStack(FluidName.fluidaa_diesel.getInstance().get(), 1000));
        addRecipe(800000, new FluidStack(FluidName.fluidaaa_diesel.getInstance().get(), 1000));
        addRecipe(1600000, new FluidStack(FluidName.fluidaaaa_diesel.getInstance().get(), 1000));


    }


    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
