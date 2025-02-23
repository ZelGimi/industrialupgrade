package com.denfop.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IInputItemStack {

    boolean matches(ItemStack var1);

    int getAmount();

    void growAmount(int col);

    List<ItemStack> getInputs();


}
