package com.denfop.api.recipe;

import net.minecraft.item.ItemStack;

public interface ISlotInv {

    boolean accepts(int index, ItemStack stack);

}
