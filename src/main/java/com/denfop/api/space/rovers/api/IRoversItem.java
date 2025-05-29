package com.denfop.api.space.rovers.api;

import com.denfop.api.space.rovers.enums.EnumRoversLevel;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public interface IRoversItem {

    String getName();

    EnumTypeRovers getType();

    IFluidHandlerItem getFluidHandler(ItemStack stack);

    EnumRoversLevel getLevel();

    double getAddProgress();

    List<EnumTypeUpgrade> getUpgradeModules();
}
