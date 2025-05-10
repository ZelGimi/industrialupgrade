package com.denfop.datagen.furnace;

import net.minecraft.world.item.ItemStack;

public class FurnaceRecipe {
    private final ItemStack input;
    private final ItemStack output;
    private final float xp;

    public FurnaceRecipe(ItemStack input, ItemStack output, float xp) {
        this.input = input;
        this.output = output;
        this.xp = xp;
    }

    public ItemStack getInput() {
        return input;
    }

    public float getXp() {
        return xp;
    }

    public ItemStack getOutput() {
        return output;
    }
}
