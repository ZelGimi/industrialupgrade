package com.denfop.invslot;

import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class CableItem {

    private final double provider;
    private final ItemStack stack;
    private int count;

    public CableItem(double provider, int count, ItemStack stack) {
        this.provider = provider;
        this.count = count;
        this.stack = stack;
    }

    public CableItem(double provider, ItemStack stack) {
        this.provider = provider;
        this.count = 1;
        this.stack = stack;
    }

    public double getProvider() {
        return provider;
    }

    public void shrink(int count) {
        this.count -= count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CableItem cableItem = (CableItem) o;
        return cableItem.getStack().is(this.stack.getItem());
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, stack, count);
    }

}
