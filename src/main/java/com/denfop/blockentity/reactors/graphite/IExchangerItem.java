package com.denfop.blockentity.reactors.graphite;

import net.minecraft.world.item.ItemStack;

public interface IExchangerItem {
    double getPercent();

    int getLevelExchanger();


    boolean damageItem(ItemStack stack, int damage);
}
