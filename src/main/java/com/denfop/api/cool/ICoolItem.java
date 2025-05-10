package com.denfop.api.cool;


import net.minecraft.world.item.ItemStack;

public interface ICoolItem {

    EnumCoolUpgrade getTypeUpgrade(ItemStack stack);

}
