package com.denfop.api.recipe;

import net.neoforged.neoforge.fluids.FluidStack;

public class RecipeFluidRemove {

    private final String nameRecipe;
    private final FluidStack stack;
    private final boolean removeAll;

    public RecipeFluidRemove(String nameRecipe, FluidStack stack, boolean removeAll) {
        this.nameRecipe = nameRecipe;
        this.stack = stack;
        this.removeAll = removeAll;
    }

    public FluidStack getStack() {
        return stack;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public boolean isRemoveAll() {
        return removeAll;
    }

}
