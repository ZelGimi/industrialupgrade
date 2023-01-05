package com.denfop.api.space.colonies;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IStorage {

    StorageBuilding getStorageBuilding();

    List<ItemStack> getStacks();

    List<FluidStack> getFluidStacks();

    int getMaxStorage();

    int getMaxStorageForFluid();

    void addMaxStorage(int storage);

    void addMaxStorageForFluid(int storage);

    boolean canAddFluidStack(FluidStack fluidStack);

    boolean canRemoveFluidStack(FluidStack fluidStack);

    boolean canAddItemStack(ItemStack fluidStack);

    boolean canRemoveItemStack(ItemStack fluidStack);

    NBTTagCompound writeNBT(NBTTagCompound tag);

    void readNBT(NBTTagCompound tag);

}
