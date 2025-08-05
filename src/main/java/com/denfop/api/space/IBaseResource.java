package com.denfop.api.space;

import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IBaseResource {

    ItemStack getItemStack();

    FluidStack getFluidStack();

    int getPercentResearchBody();

    int getChance();

    int getMaxChance();

    IBody getBody();

    int getPercentPanel();

    CompoundTag writeNBTTag(CompoundTag tagCompound, HolderLookup.Provider p_323640_);

    EnumTypeRovers getTypeRovers();

}
