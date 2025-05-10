package com.denfop.integration.jei.genmatter;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GenMatterHandler {

    private static final List<GenMatterHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final int input;


    public GenMatterHandler(
            int input, FluidStack input2
    ) {
        this.input = input;
        this.input2 = input2;
    }

    public static List<GenMatterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenMatterHandler addRecipe(
            int input, FluidStack input2
    ) {
        GenMatterHandler recipe = new GenMatterHandler(input, input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenMatterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenMatterHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(1000000, new FluidStack(FluidName.fluiduu_matter.getInstance().get(), 1000));


    }


    public int getEnergy() {
        return input;
    }

    public FluidStack getOutput() {
        return input2;
    }

}
