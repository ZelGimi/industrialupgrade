package com.denfop.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeInfo {

    private final ItemStack stack;
    private final double col;

    public RecipeInfo(ItemStack stack, double col) {
        this.stack = stack;
        this.col = col;
    }

    public RecipeInfo(NBTTagCompound tagCompound) {
        this.stack = new ItemStack(tagCompound.getCompoundTag("stack"));
        this.col = tagCompound.getDouble("matter");
    }

    public double getCol() {
        return col;
    }

    public ItemStack getStack() {
        return stack;
    }

    public NBTTagCompound writeCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound stackNBT = new NBTTagCompound();
        stack.writeToNBT(stackNBT);
        tag.setTag("stack", stackNBT);
        tag.setDouble("matter", col);
        return tag;
    }

}
