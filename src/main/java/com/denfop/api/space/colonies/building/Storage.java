package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

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

    public Storage(CompoundTag tag, HolderLookup.Provider p_323640_, final StorageBuilding storageBuilding) {
        this.maxvaluefluid = 10000;
        this.storageBuilding = storageBuilding;
        this.readNBT(tag, p_323640_);
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
            if (FluidStack.isSameFluidSameComponents(stack, fluidStack)) {
                if (stack.getAmount() + fluidStack.getAmount() <= this.maxvaluefluid) {
                    stack.grow(fluidStack.getAmount());
                    return true;
                } else if (stack.getAmount() < this.maxvaluefluid) {
                    int amount = this.maxvaluefluid - stack.getAmount();
                    stack.grow(amount);
                    fluidStack.shrink(amount);
                    added = true;
                }
            }
        }
        if (added && fluidStack.getAmount() > 0) {
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
            if (FluidStack.isSameFluidSameComponents(stack, fluidStack)) {
                if (stack.getAmount() - fluidStack.getAmount() >= 0) {
                    stack.shrink(fluidStack.getAmount());
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
            if (ModUtils.checkItemEquality(itemStack, stack)) {
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
            if (ModUtils.checkItemEquality(itemStack, stack)) {
                if (stack.getCount() - itemStack.getCount() >= 0) {
                    stack.shrink(itemStack.getCount());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CompoundTag writeNBT(final CompoundTag tag, HolderLookup.Provider p_332160_) {
        tag.putInt("max", this.max);
        tag.putInt("fluidmax", this.fluidmax);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("col", this.getStacks().size());
        for (int i = 0; i < this.getStacks().size(); i++) {
            nbt.put(String.valueOf(i), this.getStacks().get(i).save(p_332160_));
        }
        tag.put("stacks", nbt);
        CompoundTag nbt1 = new CompoundTag();
        nbt1.putInt("col_fluid", this.getFluidStacks().size());
        for (int i = 0; i < this.getFluidStacks().size(); i++) {
            if (this.fluidStackList.get(i) != null) {
                nbt1.put(String.valueOf(i), this.getFluidStacks().get(i).save(p_332160_));
            }
        }
        tag.put("fluids", nbt1);
        return tag;
    }

    public void readPacket(final CustomPacketBuffer tag) {
        this.max = tag.readInt();
        this.fluidmax = tag.readInt();
        CompoundTag nbt;
        nbt = tag.readNbt();
        assert nbt != null;
        int size = nbt.getInt("col");
        this.itemStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.itemStackList.add(ItemStack.parseOptional(tag.registryAccess(), nbt.getCompound(String.valueOf(i))));
        }

        CompoundTag nbt1;
        nbt1 = tag.readNbt();
        assert nbt1 != null;
        size = nbt1.getInt("col_fluid");
        this.fluidStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.fluidStackList.add(FluidStack.parseOptional(tag.registryAccess(), nbt1.getCompound(String.valueOf(i))));
        }

    }

    @Override
    public void readNBT(final CompoundTag tag, HolderLookup.Provider p_332160_) {
        this.max = tag.getInt("max");
        this.fluidmax = tag.getInt("fluidmax");
        CompoundTag nbt = tag.getCompound("stacks");
        int size = nbt.getInt("col");
        this.itemStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.itemStackList.add(ItemStack.parseOptional(p_332160_, nbt.getCompound(String.valueOf(i))));
        }
        CompoundTag nbt1 = tag.getCompound("fluids");
        size = nbt1.getInt("col_fluid");
        this.fluidStackList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.fluidStackList.add(FluidStack.parseOptional(p_332160_, nbt1.getCompound(String.valueOf(i))));
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
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("col", this.getStacks().size());
        for (int i = 0; i < this.getStacks().size(); i++) {
            nbt.put(String.valueOf(i), this.getStacks().get(i).save(customPacketBuffer.registryAccess()));
        }
        customPacketBuffer.writeNbt(nbt);
        CompoundTag nbt1 = new CompoundTag();
        nbt1.putInt("col_fluid", this.getStacks().size());
        for (int i = 0; i < this.getFluidStacks().size(); i++) {
            if (this.getFluidStacks().get(i) != null) {
                nbt1.put(String.valueOf(i), this.getFluidStacks().get(i).save(customPacketBuffer.registryAccess()));
            }
        }
        customPacketBuffer.writeNbt(nbt1);
    }

}
