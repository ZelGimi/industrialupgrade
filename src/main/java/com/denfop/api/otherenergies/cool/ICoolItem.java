package com.denfop.api.otherenergies.cool;


import net.minecraft.world.item.ItemStack;

public interface ICoolItem {

    EnumCoolUpgrade getTypeUpgrade(ItemStack stack);

}
