package com.denfop.api.space;

import net.minecraft.item.ItemStack;

public interface IBaseResource {

    ItemStack getItemStack();

    int getChance();

    int getMaxChance();

    IBody getBody();

    int getPercentPanel();

}
