package com.denfop.api.upgrade;

import net.minecraft.item.ItemStack;

public class UpgradeModificator {

    public final ItemStack itemstack;
    public final String type;

    public UpgradeModificator(ItemStack stack, String type) {
        this.itemstack = stack;
        this.type = type;
    }

    public boolean matches(ItemStack stack) {
        return this.itemstack.isItemEqual(stack);
    }

    public boolean matches(String type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpgradeModificator that = (UpgradeModificator) o;
        return itemstack.isItemEqual(that.itemstack);
    }


}
