package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.building.StorageBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

    CompoundTag writeNBT(CompoundTag tag);

    void readNBT(CompoundTag tag);

    boolean work();

    void writePacket(CustomPacketBuffer customPacketBuffer);
}
