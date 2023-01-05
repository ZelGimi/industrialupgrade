package com.denfop.api.cool;

import net.minecraft.item.ItemStack;

public interface ICoolItem {

    EnumCoolUpgrade getTypeUpgrade(ItemStack stack);

}
