package com.denfop.api.space.rovers.api;

import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import net.minecraft.world.item.ItemStack;

public interface IRovers {

    String getName();

    EnumTypeRovers getType();

    IRoversItem getItem();

    ItemStack getItemStack();

}
