package com.denfop.integration.jei.biomass;


import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BiomassHandler {

    private static final List<BiomassHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack output;

    public BiomassHandler(
            ItemStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<BiomassHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BiomassHandler addRecipe(
            ItemStack input,
            FluidStack output
    ) {
        BiomassHandler recipe = new BiomassHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(
                IUItem.biochaff,
                new FluidStack(FluidName.fluidbiomass.getInstance(), 200)
        );
        addRecipe(
                IUItem.plantBall,
                new FluidStack(FluidName.fluidbiomass.getInstance(), 150)
        );
    }

    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
