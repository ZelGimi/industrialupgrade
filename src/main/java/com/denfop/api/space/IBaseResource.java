package com.denfop.api.space;

import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public interface IBaseResource {

    ItemStack getItemStack();

    FluidStack getFluidStack();

    int getPercentResearchBody();

    int getChance();

    int getMaxChance();

    IBody getBody();

    int getPercentPanel();

    NBTTagCompound writeNBTTag(NBTTagCompound tagCompound);

    EnumTypeRovers getTypeRovers();

}
