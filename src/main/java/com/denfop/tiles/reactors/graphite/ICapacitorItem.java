package com.denfop.tiles.reactors.graphite;


import net.minecraft.world.item.ItemStack;

public interface ICapacitorItem {

    double getPercent();

    int getLevelCapacitor();

    boolean damageItem(ItemStack stack, int damage);

}
