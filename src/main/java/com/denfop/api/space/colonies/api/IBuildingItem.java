package com.denfop.api.space.colonies.api;

import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import net.minecraft.item.ItemStack;

public interface IBuildingItem {

    EnumTypeBuilding getBuilding(ItemStack stack);

    IColonyBuilding getBuilding(IColony colony, ItemStack stack, boolean simulate);

}
