package com.denfop.container;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SlotInfo extends InvSlot implements VirtualSlot {


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
    public void readFromNbt(HolderLookup.Provider provider, final CompoundTag nbt) {
        super.readFromNbt(provider, nbt);
        fluid = nbt.getBoolean("fluid");
        if (this.fluid) {
            fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), null));

            for (int i = 0; i < size(); i++) {
                if (!this.get(i).isEmpty()) {
                    Item item = this.get(i).getItem();
                    Block block = Block.byItem(item);
                    if (block != Blocks.AIR) {
                        if (block instanceof LiquidBlock) {
                            fluidStackList.set(i, new FluidStack(((LiquidBlock) block).fluid.getSource(), 1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag writeToNbt(HolderLookup.Provider provider, CompoundTag nbt) {
        nbt = super.writeToNbt(provider, nbt);
        nbt.putBoolean("fluid", isFluid());
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
    public boolean accepts(final ItemStack stack, final int index) {
        return true;
    }

    public ItemStack set(int index, ItemStack stack) {
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
        this.onChanged();
        return stack;
    }

    public void onChanged() {
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
