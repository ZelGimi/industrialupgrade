package com.denfop.items.bags;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class BagsDescription {

    private final ItemStack stack;
    int count;

    public BagsDescription(ItemStack stack) {
        this.stack = stack;
        this.count = stack.getCount();
    }

    public BagsDescription(CompoundTag tagCompound) {
        this.stack = ItemStack.of(tagCompound.getCompound("item"));
        this.count = tagCompound.getInt("count");
    }

    public CompoundTag write(CompoundTag tagCompound) {
        tagCompound.put("item", this.stack.serializeNBT());
        tagCompound.putInt("count", this.count);
        return tagCompound;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void addCount(int count) {
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
        return stack.getItem() == that.stack.getItem();
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }

}
