package com.denfop.integration.jei.genneutronium;


import com.denfop.Config;
import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenNeuHandler {

    private static final List<GenNeuHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final double input;


    public GenNeuHandler(
            double input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenNeuHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenNeuHandler addRecipe(
            double input, FluidStack input2
    ) {
        GenNeuHandler recipe = new GenNeuHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenNeuHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenNeuHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(Config.energy * 1000, new FluidStack(FluidName.fluidNeutron.getInstance(), 1000));


    }


    public double getEnergy() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
