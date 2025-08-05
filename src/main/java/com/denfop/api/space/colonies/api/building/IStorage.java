package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.building.StorageBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

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

    CompoundTag writeNBT(CompoundTag tag, HolderLookup.Provider p_332160_);

    void readNBT(CompoundTag tag, HolderLookup.Provider p_332160_);

    boolean work();

    void writePacket(CustomPacketBuffer customPacketBuffer);
}
