package com.denfop.container;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.VirtualSlot;
import com.denfop.blocks.FluidName;
import com.denfop.invslot.InvSlot;
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

    private final VirtualSlot slotInfo;
    private final int index;

    public SlotVirtual(
            final IAdvInventory inventoryIn,
            final int index,
            final int xPosition,
            final int yPosition,
            VirtualSlot slotInfo
    ) {
        super(
                inventoryIn,
                slotInfo instanceof InvSlot ? inventoryIn.getBaseIndex((InvSlot) slotInfo) + index : index,
                xPosition,
                yPosition
        );
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
                ItemStack stack = itemstack12.copy();
                stack.setCount(1);
                if (this.isItemValid(stack)) {
                    putStack(stack);
                }
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
                            ItemStack fluidItemStack = new ItemStack(containerFluid.getFluid().getBlock());
                            if (containerFluid.getFluid() == FluidRegistry.WATER) {
                                fluidItemStack = new ItemStack(FluidName.fluidwater.getInstance().getBlock());
                            }
                            if (containerFluid.getFluid() == FluidRegistry.LAVA) {
                                fluidItemStack = new ItemStack(FluidName.fluidlava.getInstance().getBlock());
                            }
                            this.inventory.setInventorySlotContents(this.getSlotIndex(), fluidItemStack);
                            if (this.slotInfo.getFluidStackList() == null || this.slotInfo.getFluidStackList().isEmpty()) {
                                this.slotInfo.setFluidList(new ArrayList<>(Collections.nCopies(this.slotInfo.size(), null)));
                            }
                            this.slotInfo.getFluidStackList().set(index, containerFluid);
                        }
                    }
                }
            }
        } else {

            if (this.slotInfo.isFluid()) {
                Block block = Block.getBlockFromItem(this.slotInfo.get(index).getItem());
                if (block != Blocks.AIR) {
                    if (block instanceof IFluidBlock) {
                        this.slotInfo.getFluidStackList().set(index, null);
                    }
                }
            }
            putStack(itemstack12);
        }
        this.onSlotChanged();
    }

    public boolean isItemValid(ItemStack stack) {
        return this.slotInfo.accepts(stack, index);
    }

}
