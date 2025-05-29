package com.denfop.api.windsystem;


import net.minecraft.world.item.ItemStack;

public interface IWindUpgradeBlock {

    IWindRotor getRotor();

    ItemStack getItemStack();

}
