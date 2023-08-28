package com.denfop.api.recipe;

import net.minecraft.item.ItemStack;

public class RecipeRemove {

    private final String nameRecipe;
    private final ItemStack stack;
    private final boolean removeAll;

    public RecipeRemove(String nameRecipe, ItemStack stack, boolean removeAll) {
        this.nameRecipe = nameRecipe;
        this.stack = stack;
        this.removeAll = removeAll;
    }

    public ItemStack getStack() {
        return stack;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public boolean isRemoveAll() {
        return removeAll;
    }

}
