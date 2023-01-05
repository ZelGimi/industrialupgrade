package com.denfop.items.bags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class BagsDescription {

    private final ItemStack stack;
    int count;

    public BagsDescription(ItemStack stack) {
        this.stack = stack;
        this.count = stack.getCount();
    }

    public BagsDescription(NBTTagCompound tagCompound) {
        this.stack = new ItemStack(tagCompound.getCompoundTag("item"));
        this.count = tagCompound.getInteger("count");
    }

    public NBTTagCompound write(NBTTagCompound tagCompound) {
        tagCompound.setTag("item", this.stack.serializeNBT());
        tagCompound.setInteger("count", this.count);
        return tagCompound;
    }

    public ItemStack getStack() {
        return stack;
    }

    void addCount(int count) {
        this.count += count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BagsDescription that = (BagsDescription) o;
        return stack.getItem() == that.stack.getItem() && (stack.getItemDamage() == that.stack.getItemDamage() || (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && that.stack.getItemDamage() == OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }

}
