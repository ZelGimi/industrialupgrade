package com.denfop.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IRecipeInputStack {

    List<ItemStack> getItemStack();

    boolean matched(ItemStack stack);

}
