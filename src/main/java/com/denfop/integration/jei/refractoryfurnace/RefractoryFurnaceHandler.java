package com.denfop.integration.jei.refractoryfurnace;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class RefractoryFurnaceHandler {

    private static final List<RefractoryFurnaceHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final FluidStack output;

    public RefractoryFurnaceHandler(
            ItemStack input,
            FluidStack output
    ) {
        this.input = input;
        this.output = output;
    }

    public static List<RefractoryFurnaceHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RefractoryFurnaceHandler addRecipe(
            ItemStack input,
            FluidStack output
    ) {
        RefractoryFurnaceHandler recipe = new RefractoryFurnaceHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseFluidMachineRecipe container : Recipes.recipes.getRecipeFluid().getRecipeList("refractory_furnace")) {

            addRecipe(
                    container.input.getStack().getInputs().get(0),
                    container.getOutput_fluid().get(0)
            );

        }

    }

    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }


}
