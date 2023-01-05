package com.denfop.api.space.colonies;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class Storage implements IStorage {

    private final StorageBuilding storageBuilding;
    private final int maxvaluefluid;
    List<ItemStack> itemStackList;
    List<FluidStack> fluidStackList;
    private int max;
    private int fluidmax;

    public Storage(final StorageBuilding storageBuilding) {
        this.itemStackList = new ArrayList<>();
        this.fluidStackList = new ArrayList<>();
        this.max = 5;
        this.fluidmax = 10;
        this.maxvaluefluid = 10000;
        this.storageBuilding = storageBuilding;
    }

    public Storage(NBTTagCompound tag, final StorageBuilding storageBuilding) {
        this.maxvaluefluid = 10000;
        this.storageBuilding = storageBuilding;
        this.readNBT(tag);
    }

    @Override
    public StorageBuilding getStorageBuilding() {
        return this.storageBuilding;
    }

    @Override
    public List<ItemStack> getStacks() {
        return this.itemStackList;
    }

    @Override
    public List<FluidStack> getFluidStacks() {
        return this.fluidStackList;
    }

    @Override
    public int getMaxStorage() {
        return this.max;
    }

    @Override
    public int getMaxStorageForFluid() {
        return this.fluidmax;
    }

    @Override
    public void addMaxStorage(final int storage) {
        this.max += storage;
    }

    @Override
    public void addMaxStorageForFluid(final int storage) {
        this.fluidmax += storage;
    }

    @Override
    public boolean canAddFluidStack(final FluidStack fluidStack) {
        if (!this.getStorageBuilding().getWork()) {
            return false;
        }
        List<FluidStack> fluidStacks = this.getFluidStacks();
        for (FluidStack stack : fluidStacks) {
            if (stack.isFluidEqual(fluidStack)) {
                if (stack.amount + fluidStack.amount <= this.maxvaluefluid) {
                    stack.amount += fluidStack.amount;
                    return true;
                } else {
                    if (this.getFluidStacks().size() < this.fluidmax) {
                        int temp = this.maxvaluefluid - stack.amount;
                        stack.amount += temp;
                        fluidStack.amount -= temp;
                        fluidStacks.add(fluidStack);
                        return true;
                    }
                }
            }
        }
        if (this.getFluidStacks().size() >= this.fluidmax) {
            return false;
        } else {
            this.getFluidStacks().add(fluidStack);
            return true;
        }

    }

    @Override
    public boolean canRemoveFluidStack(final FluidStack fluidStack) {
        if (!this.getStorageBuilding().getWork()) {
            return false;
        }
        List<FluidStack> fluidStacks = this.getFluidStacks();
        for (FluidStack stack : fluidStacks) {
            if (stack.isFluidEqual(fluidStack)) {
                if (stack.amount - fluidStack.amount >= 0) {
                    stack.amount -= fluidStack.amount;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canAddItemStack(final ItemStack itemStack) {
        if (!this.getStorageBuilding().getWork()) {
            return false;
        }
        List<ItemStack> stackList = this.getStacks();
        int size = itemStack.getCount();
        for (ItemStack stack : stackList) {
            if (stack.isItemEqual(itemStack)) {
                if (stack.getCount() + size <= 64) {
                    stack.grow(size);
                    return true;
                } else {
                    if (this.getStacks().size() < this.max) {
                        int temp = 64 - stack.getCount();
                        stack.grow(temp);
                        itemStack.shrink(temp);
                        itemStackList.add(itemStack);
                        return true;
                    }
                }
            }
        }
        if (this.getStacks().size() >= this.max) {
            return false;
        } else {
            this.getStacks().add(itemStack);
            return true;
        }
    }

    @Override
    public boolean canRemoveItemStack(final ItemStack itemStack) {
        if (!this.getStorageBuilding().getWork()) {
            return false;
        }
        List<ItemStack> itemStackList = this.getStacks();
        for (ItemStack stack : itemStackList) {
            if (stack.isItemEqual(itemStack)) {
                if (stack.getCount() - itemStack.getCount() >= 0) {
                    stack.shrink(itemStack.getCount());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound writeNBT(final NBTTagCompound tag) {
        tag.setInteger("max", this.max);
        tag.setInteger("fluidmax", this.fluidmax);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("col", this.getStacks().size());
        for (int i = 0; i < this.getStacks().size(); i++) {
            nbt.setTag(String.valueOf(i), this.getStacks().get(i).writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("stacks", nbt);
        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("col_fluid", this.getStacks().size());
        for (int i = 0; i < this.getFluidStacks().size(); i++) {
            nbt1.setTag(String.valueOf(i), this.getFluidStacks().get(i).writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("fluids", nbt1);
        return tag;
    }

    @Override
    public void readNBT(final NBTTagCompound tag) {
        this.max = tag.getInteger("max");
        this.fluidmax = tag.getInteger("fluidmax");
        NBTTagCompound nbt = tag.getCompoundTag("stacks");
        int size = nbt.getInteger("col");
        this.itemStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.itemStackList.add(new ItemStack(nbt.getCompoundTag(String.valueOf(i))));
        }
        NBTTagCompound nbt1 = tag.getCompoundTag("fluids");
        size = nbt1.getInteger("col_fluid");
        this.fluidStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.fluidStackList.add(FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(String.valueOf(i))));
        }
    }

}
