package com.denfop.container;

import com.denfop.api.inv.IAdvInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.ArrayList;
import java.util.Collections;

public class SlotVirtual extends Slot {

    private final SlotInfo slotInfo;
    private final int index;

    public SlotVirtual(
            final IAdvInventory inventoryIn,
            final int index,
            final int xPosition,
            final int yPosition,
            SlotInfo slotInfo
    ) {
        super(inventoryIn, inventoryIn.getBaseIndex(slotInfo) + index, xPosition, yPosition);
        this.slotInfo = slotInfo;
        this.index = index;
    }

    public ItemStack getStack() {
        return this.slotInfo.get(this.index);
    }

    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }
    @Override
    public void putStack(final ItemStack stack) {
        super.putStack(stack);
    }

    public void slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {

        ItemStack itemstack12 = player.inventory.getItemStack();
        if (!itemstack12.isEmpty()) {
            if (!this.slotInfo.isFluid()) {
                this.inventory.setInventorySlotContents(index, itemstack12);
            } else {
                itemstack12 = itemstack12.copy();
                itemstack12.setCount(1);
                if (FluidUtil.getFluidHandler(itemstack12) != null) {
                    IFluidHandlerItem handler = itemstack12.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );
                    if (handler != null) {
                        final FluidStack containerFluid = handler.drain(2147483647, false);
                        if (containerFluid != null && containerFluid.amount > 0) {
                            final ItemStack fluidItemStack = new ItemStack(containerFluid.getFluid().getBlock());
                            this.inventory.setInventorySlotContents(index,fluidItemStack );
                            if (this.slotInfo.fluidStackList == null || this.slotInfo.fluidStackList.size() == 0) {
                                this.slotInfo.fluidStackList = new ArrayList<>(Collections.nCopies(this.slotInfo.size(), null));
                            }
                            this.slotInfo.fluidStackList.set(index, containerFluid);
                        }
                    }
                }
            }
        } else {

            if (this.slotInfo.isFluid()) {
                Block block = Block.getBlockFromItem(this.slotInfo.get(index).getItem());
                if (block != Blocks.AIR) {
                    if (block instanceof IFluidBlock) {
                        this.slotInfo.fluidStackList.set(index, null);
                    }
                }
            }
            this.inventory.setInventorySlotContents(index, itemstack12);
        }
        this.onSlotChanged();
    }

    public boolean isItemValid(ItemStack stack) {
        return this.slotInfo.accepts(stack, index);
    }

}
