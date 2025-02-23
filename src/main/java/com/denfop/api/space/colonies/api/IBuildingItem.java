package com.denfop.api.space.colonies.api;

import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface IBuildingItem {

    EnumTypeBuilding getBuilding(ItemStack stack);

    IColonyBuilding getBuilding(IColony colony, ItemStack stack, boolean simulate);
}
