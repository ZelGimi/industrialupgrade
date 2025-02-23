package com.denfop.tiles.reactors.graphite;

import net.minecraft.item.ItemStack;

public interface IExchangerItem {

    double getPercent();

    int getLevel();

    boolean damageItem(ItemStack stack, int damage);

}
