package com.denfop.container;

import com.denfop.invslot.InvSlot;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotInfo extends InvSlot {


    private boolean fluid;
    List<FluidStack> fluidStackList;

    public SlotInfo(TileEntityMultiCable multiCable, int size, boolean fluid) {
        super(multiCable, null, size);
        this.fluid = fluid;
        this.fluidStackList = new ArrayList<>(Collections.nCopies(this.size(), null));

    }

    @Override
    public void readFromNbt(final NBTTagCompound nbt) {
        super.readFromNbt(nbt);
        fluid = nbt.getBoolean("fluid");
        if (this.fluid) {
            fluidStackList =  new ArrayList<>(Collections.nCopies(this.size(), null));

            for (int i = 0; i < size(); i++) {
                if (!this.get(i).isEmpty()) {
                    Block block = Block.getBlockFromItem(this.get(i).getItem());
                    if(block != Blocks.AIR){
                        if(block instanceof IFluidBlock){
                            fluidStackList.set(i,new FluidStack (((IFluidBlock) block).getFluid(),1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
        nbt =  super.writeToNbt(nbt);
        nbt.setBoolean("fluid",isFluid());
        return nbt;
    }

    public List<FluidStack> getFluidStackList() {
        return fluidStackList;
    }

    public boolean isFluid() {
        return fluid;
    }

    public void setFluid(final boolean fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return false;
    }

    public void put(int index, ItemStack stack) {
        stack = stack.copy();
        stack.setCount(1);
        this.contents.set(index, stack);
    }


    public ItemStack get(int index) {
        return this.contents.get(index);
    }

    public ItemStack get() {
        return this.get(0);
    }

}
