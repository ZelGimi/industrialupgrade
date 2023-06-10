package com.denfop.componets;

import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

public enum TypeUpgrade {
    STACK(new ItemStack(IUItem.module_stack)),
    INSTANT(new ItemStack(IUItem.module_quickly)),
    SORTING(new ItemStack(IUItem.module_storage));
    private final ItemStack stack;

    TypeUpgrade(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
}
