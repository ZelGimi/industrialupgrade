package com.denfop.container;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SlotInfo extends Inventory implements VirtualSlot {


    List<FluidStack> fluidStackList;
    private List<ItemStack> listBlack;
    private List<ItemStack> listWhite;
    private boolean fluid;

    public SlotInfo(TileEntityInventory multiCable, int size, boolean fluid) {
        super(multiCable, null, size);
        this.fluid = fluid;
        this.fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), null));
        this.listBlack = new ArrayList<>();
        this.listWhite = new ArrayList<>();
    }

    public List<ItemStack> getListBlack() {
        return listBlack;
    }

    public List<ItemStack> getListWhite() {
        return listWhite;
    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        fluid = nbt.getBoolean("fluid");
        if (this.fluid) {
            fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), null));

            for (int i = 0; i < size(); i++) {
                if (!this.get(i).isEmpty()) {
                    Block block = Block.getBlockFromItem(this.get(i).getItem());
                    if (block != Blocks.AIR) {
                        if (block instanceof IFluidBlock) {
                            fluidStackList.set(i, new FluidStack(((IFluidBlock) block).getFluid(), 1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean accepts(final int index, final ItemStack stack) {
        return this.isItemValidForSlot(index,stack);
    }

    @Override
    public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
        nbt = super.writeToNbt(nbt);
        nbt.setBoolean("fluid", isFluid());
        return nbt;
    }

    public List<FluidStack> getFluidStackList() {
        return fluidStackList;
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {
        this.fluidStackList = fluidStackList;
    }

    public boolean isFluid() {
        return fluid;
    }

    public void setFluid(final boolean fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }

    public void put(int index, ItemStack stack) {
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
        }

        this.contents.set(index, stack);
        listBlack.clear();
        listWhite.clear();
        this.listBlack = new LinkedList<>();
        this.listWhite = new LinkedList<>();
        for (int i = 0; i < size(); i++) {
            ItemStack itemStack = this.contents.get(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (i < 9) {
                listBlack.add(itemStack);
            } else {
                listWhite.add(itemStack);
            }
        }

        this.listBlack = new ArrayList<>(listBlack);
        this.listWhite = new ArrayList<>(listWhite);
        this.markDirty();
    }

    public void markDirty() {
        listBlack.clear();
        listWhite.clear();
        for (int i = 0; i < size(); i++) {
            ItemStack itemStack = this.contents.get(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (i < 9) {
                listBlack.add(itemStack);
            } else {
                listWhite.add(itemStack);
            }
        }
    }
    public ItemStack get(int index) {
        return this.contents.get(index);
    }

    public ItemStack get() {
        return this.get(0);
    }

}
