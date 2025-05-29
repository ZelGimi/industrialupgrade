package com.denfop.container;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

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
        this.fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), FluidStack.EMPTY));
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
    public void readFromNbt(final CompoundTag nbt) {
        super.readFromNbt(nbt);
        fluid = nbt.getBoolean("fluid");
        if (this.fluid) {
            fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), FluidStack.EMPTY));

            for (int i = 0; i < size(); i++) {
                if (!this.get(i).isEmpty()) {
                    if (FluidHandlerFix.hasFluidHandler(this.get(i))) {
                        fluidStackList.set(i, new FluidStack(FluidHandlerFix.getFluidHandler(this.get(i)).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), 1));
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag writeToNbt(CompoundTag nbt) {
        nbt = super.writeToNbt(nbt);
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

    protected void putFromNBT(int index, ItemStack content) {
        this.contents.set(index, content);
        listBlack.clear();
        listWhite.clear();
        for (int i = 0; i < size(); i++) {
            ItemStack itemStack = this.contents.get(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (index < 9) {
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
