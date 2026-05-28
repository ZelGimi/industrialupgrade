package com.denfop.api.storage.cell;

import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.autocrafting.SameStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public interface ICell {

    Map<String, List<StorageStack>> getStorageStack();

    ItemStack[] getStacks();

    FluidStack[] getFluids();

    CellInfo getCellInfo();

    int canAdd(ItemStack stack);

    int canAdd(Item stack, CompoundTag tag, int amount);

    int canAdd(Item stack, CompoundTag tag, int amount, StorageStack storageStack);

    int canAdd(Fluid stack, CompoundTag tag, int amount, StorageStack storageStack);

    int canAdd(Fluid stack, CompoundTag tag, int amount);

    int canAdd(FluidStack stack);

    int add(ItemStack stack, boolean simulate);

    ItemStack removeStack(ItemStack request);

    ItemStack addStack(ItemStack stack);

    ItemStack removeItem(ItemStack request);

    int getStorage();

    boolean needUpdate();

    void save();

    void setUpdate(boolean update);

    List<StorageStack> getStorageStackFromItem(ItemStack stack);

    List<StorageStack> getStorageStackFromFluid(FluidStack stack);

    int addFluidDirect(FluidStack stack, boolean simulate);

    void addFluidCrafting(Fluid fluid, CompoundTag tag, int amount);

    FluidStack addFluid(FluidStack stack);

    int removeFluid(FluidStack request);

    int canAddFluid(FluidStack stack);

    ItemStack removeStackWithIgnoring(Item partialRequest, CompoundTag tag, int amount);

    int removeStackWithIgnoringAmount(Item partialRequest, CompoundTag tag, int amount);

    void addStack(Item item, CompoundTag tag, int amount);

    void addStackCrafting(Item item, CompoundTag tag, int amount);

    void addStack(Item item, CompoundTag tag, int amount, StorageStack storageStack);

    void addFluid(Fluid item, CompoundTag tag, int amount, StorageStack storageStack);

    void addFluid(Fluid fluid, CompoundTag tag, int amount);

    int removeStack(Item item, CompoundTag tag, int count);

    int removeStackCrafting(Item item, CompoundTag tag, int count);

    int removeFluid(Fluid item, CompoundTag tag, int remaining);

    int removeFluidCrafting(Fluid item, CompoundTag tag, int remaining);

    int removeStackWithIgnoringAmountCrafting(Item request, CompoundTag tag, int remaining);

    int getItemsForCraft(CompoundTag tag, List<StorageStack> list);

    Map<Integer, Integer> getStacksForCraft();

    int removeFluidFromListCraft(SameStack key, Integer value);

    int removeItemFromListCraft(SameStack key, Integer value);
}
