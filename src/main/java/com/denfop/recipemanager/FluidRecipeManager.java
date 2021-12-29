package com.denfop.recipemanager;

import com.denfop.api.IFluidRecipeManager;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class FluidRecipeManager implements IFluidRecipeManager {
    private final Map<IFluidRecipeManager.Input, FluidStack[]> recipes = new HashMap<>();

    @Override
    public void addRecipe(FluidStack fluidStack, FluidStack[] output) {

        if (output == null)
            throw new NullPointerException("The recipe output is null");
        if (fluidStack == null)
            throw new NullPointerException("The fluidStack is null");

        this.recipes.put(new IFluidRecipeManager.Input(fluidStack), output);
    }

    @Override
    public Map<IFluidRecipeManager.Input, FluidStack[]> getRecipes() {
        return this.recipes;
    }

}
