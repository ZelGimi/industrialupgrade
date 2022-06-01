package com.denfop.api.cooling;

import net.minecraft.item.ItemStack;

public interface ICoolItem {

    EnumCoolUpgrade getTypeUpgrade(ItemStack stack);

}
