package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Storage implements IStorage {

    private final StorageBuilding storageBuilding;
    private final int maxvaluefluid;
    List<ItemStack> itemStackList;
    List<FluidStack> fluidStackList;
    private int max;
    private int fluidmax;

    public Storage(final StorageBuilding storageBuilding) {
        this.itemStackList = new LinkedList<>();
        this.fluidStackList = new LinkedList<>();
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

    public Storage(CustomPacketBuffer packetBuffer, final StorageBuilding storageBuilding) {
        this.maxvaluefluid = 10000;
        this.storageBuilding = storageBuilding;
        this.readPacket(packetBuffer);
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
        if (fluidStack == null) {
            return false;
        }
        List<FluidStack> fluidStacks = this.getFluidStacks();
        boolean added = false;
        fluidStacks.removeIf(Objects::isNull);
        for (FluidStack stack : fluidStacks) {
            if (stack.isFluidEqual(fluidStack)) {
                if (stack.amount + fluidStack.amount <= this.maxvaluefluid) {
                    stack.amount += fluidStack.amount;
                    return true;
                } else if (stack.amount < this.maxvaluefluid) {
                    int amount = this.maxvaluefluid - stack.amount;
                    stack.amount += amount;
                    fluidStack.amount -= amount;
                    added = true;
                }
            }
        }
        if (added && fluidStack.amount > 0) {
            if (this.getFluidStacks().size() < this.fluidmax) {
                fluidStacks.add(fluidStack);
                return true;
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
        boolean added = false;
        for (ItemStack stack : stackList) {
            if (stack.isItemEqual(itemStack)) {
                if (stack.getCount() + size <= stack.getMaxStackSize()) {
                    stack.grow(size);
                    return true;
                } else if (stack.getCount() < stack.getMaxStackSize()) {
                    int amount = stack.getMaxStackSize() - stack.getCount();
                    stack.grow(amount);
                    itemStack.shrink(amount);
                    size = itemStack.getCount();
                    added = true;
                }
            }
        }
        if (added && itemStack.getCount() > 0) {
            if (this.getStacks().size() < this.max) {
                itemStackList.add(itemStack);
                return true;
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
        nbt1.setInteger("col_fluid", this.getFluidStacks().size());
        for (int i = 0; i < this.getFluidStacks().size(); i++) {
            if (this.fluidStackList.get(i) != null) {
                nbt1.setTag(String.valueOf(i), this.getFluidStacks().get(i).writeToNBT(new NBTTagCompound()));
            }
        }
        tag.setTag("fluids", nbt1);
        return tag;
    }

    public void readPacket(final CustomPacketBuffer tag) {
        this.max = tag.readInt();
        this.fluidmax = tag.readInt();
        NBTTagCompound nbt;
        try {
            nbt = tag.readCompoundTag();
            assert nbt != null;
            int size = nbt.getInteger("col");
            this.itemStackList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                this.itemStackList.add(new ItemStack(nbt.getCompoundTag(String.valueOf(i))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NBTTagCompound nbt1;
        try {
            nbt1 = tag.readCompoundTag();
            assert nbt1 != null;
            final int size = nbt1.getInteger("col_fluid");
            this.fluidStackList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                this.fluidStackList.add(FluidStack.loadFluidStackFromNBT(nbt1.getCompoundTag(String.valueOf(i))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            this.fluidStackList.add(FluidStack.loadFluidStackFromNBT(nbt1.getCompoundTag(String.valueOf(i))));
        }
    }

    @Override
    public boolean work() {
        return storageBuilding.work;
    }

    @Override
    public void writePacket(final CustomPacketBuffer customPacketBuffer) {
        customPacketBuffer.writeInt(max);
        customPacketBuffer.writeInt(fluidmax);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("col", this.getStacks().size());
        for (int i = 0; i < this.getStacks().size(); i++) {
            nbt.setTag(String.valueOf(i), this.getStacks().get(i).writeToNBT(new NBTTagCompound()));
        }
        customPacketBuffer.writeCompoundTag(nbt);
        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("col_fluid", this.getStacks().size());
        for (int i = 0; i < this.getFluidStacks().size(); i++) {
            if (this.getFluidStacks().get(i) != null) {
                nbt1.setTag(String.valueOf(i), this.getFluidStacks().get(i).writeToNBT(new NBTTagCompound()));
            }
        }
        customPacketBuffer.writeCompoundTag(nbt1);
    }

}
