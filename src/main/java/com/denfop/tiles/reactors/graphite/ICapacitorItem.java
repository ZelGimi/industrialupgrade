package com.denfop.tiles.reactors.graphite;

import net.minecraft.item.ItemStack;

public interface ICapacitorItem {

    double getPercent();

    int getLevel();

    void damageItem(ItemStack stack, int damage);

}
