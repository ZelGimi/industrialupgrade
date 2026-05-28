package com.denfop.api.storage.cell;

import com.denfop.api.storage.StorageStack;
import com.denfop.api.storage.autocrafting.SameStack;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public interface ICell {

    Map<String, List<StorageStack>> getStorageStack();

    ItemStack[] getStacks();

    FluidStack[] getFluids();

    CellInfo getCellInfo();

    int canAddStack(ItemStack stack);

    int canAddStack(Item stack, DataComponentMap tag, int amount);

    int canAddStack(Item stack, DataComponentMap tag, int amount, StorageStack storageStack);

    int canAddStack(Fluid stack, DataComponentMap tag, int amount, StorageStack storageStack);

    int canAddStack(Fluid stack, DataComponentMap tag, int amount);

    int canAddStack(FluidStack stack);

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

    void addFluidCrafting(Fluid fluid, DataComponentMap tag, int amount);

    FluidStack addFluid(FluidStack stack);

    int removeFluid(FluidStack request);

    int canAddFluid(FluidStack stack);

    ItemStack removeStackWithIgnoring(Item partialRequest, DataComponentMap tag, int amount);

    int removeStackWithIgnoringAmount(Item partialRequest, DataComponentMap tag, int amount);

    void addStack(Item item, DataComponentMap tag, int amount);

    void addStackCrafting(Item item, DataComponentMap tag, int amount);

    void addStack(Item item, DataComponentMap tag, int amount, StorageStack storageStack);

    void addFluid(Fluid item, DataComponentMap tag, int amount, StorageStack storageStack);

    void addFluid(Fluid fluid, DataComponentMap tag, int amount);

    int removeStack(Item item, DataComponentMap tag, int count);

    int removeStackCrafting(Item item, DataComponentMap tag, int count);

    int removeFluid(Fluid item, DataComponentMap tag, int remaining);

    int removeFluidCrafting(Fluid item, DataComponentMap tag, int remaining);

    int removeStackWithIgnoringAmountCrafting(Item request, DataComponentMap tag, int remaining);

    int getItemsForCraft(DataComponentMap tag, List<StorageStack> list);

    Map<Integer, Integer> getStacksForCraft();

    int removeFluidFromListCraft(SameStack key, Integer value);

    int removeItemFromListCraft(SameStack key, Integer value);
}
