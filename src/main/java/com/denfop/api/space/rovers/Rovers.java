package com.denfop.api.space.rovers;

import net.minecraft.item.ItemStack;

public class Rovers implements IRovers {

    private final IRoversItem item;
    private final ItemStack stack;

    public Rovers(IRoversItem item, ItemStack stack) {
        this.item = item;
        this.stack = stack;
    }

    @Override
    public String getName() {
        return this.item.getName();
    }

    @Override
    public EnumTypeRovers getType() {
        return item.getType();
    }

    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rovers rovers = (Rovers) o;
        return stack.isItemEqual(rovers.stack);
    }


}
