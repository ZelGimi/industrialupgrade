package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.dataregistry.DataSimpleItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum TypeUpgrade {
    STACK(IUItem.module_stack),
    INSTANT(IUItem.module_quickly),
    SORTING(IUItem.module_storage);
    private final DataSimpleItem<? extends Item, ? extends ResourceLocation> stack;

    TypeUpgrade(DataSimpleItem<? extends Item, ? extends ResourceLocation> stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return new ItemStack(stack.getItem());
    }
}
