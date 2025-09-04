package com.denfop.containermenu.slot;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.menu.VirtualSlot;
import com.denfop.inventory.Inventory;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class SlotVirtual extends Slot {
    private final VirtualSlot slotInfo;
    private final int index;

    public SlotVirtual(final CustomWorldContainer inventoryIn,
                       final int index,
                       final int xPosition,
                       final int yPosition,
                       VirtualSlot slotInfo) {
        super(inventoryIn,
                slotInfo instanceof Inventory ? inventoryIn.getBaseIndex((Inventory) slotInfo) + index : index,
                xPosition,
                yPosition);
        this.slotInfo = slotInfo;
        this.index = index;
    }

    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return this.slotInfo.canPlaceItem(this.index, itemStack);
    }

    public int getJeiX() {
        return this.x - 1;
    }

    public int getJeiY() {
        return this.y - 1;
    }

    public @NotNull ItemStack getItem() {
        return this.slotInfo.get(this.index);
    }

    public void set(@NotNull ItemStack itemStack) {
        super.set(itemStack);
    }

    @Override
    public boolean mayPickup(Player p_40228_) {
        return false;
    }


    public void slotClick(int slotId, int dragType, ClickType clickType, Player player) {
        ItemStack itemstack12 = player.containerMenu.getCarried();
        if (!itemstack12.isEmpty()) {
            if (!this.slotInfo.isFluid()) {
                ItemStack stack = itemstack12.copy();
                stack.setCount(1);
                if (this.mayPlace(stack)) {
                    set(stack);
                }
            } else {
                itemstack12 = itemstack12.copy();
                itemstack12.setCount(1);
                IFluidHandlerItem handler = null;
                try {
                    handler = FluidHandlerFix.getFluidHandler(itemstack12);
                } catch (Exception e) {
                }
                ;
                if (handler != null) {


                    final FluidStack containerFluid = handler.drain(2147483647, IFluidHandler.FluidAction.SIMULATE);
                    if (!containerFluid.isEmpty() && containerFluid.getAmount() > 0) {

                        this.container.setItem(this.getSlotIndex(), itemstack12);
                        if (this.slotInfo.getFluidStackList() == null || this.slotInfo.getFluidStackList().isEmpty()) {
                            this.slotInfo.setFluidList(new ArrayList<>(Collections.nCopies(this.slotInfo.size(), FluidStack.EMPTY)));
                        }
                        this.slotInfo.getFluidStackList().set(index, containerFluid);
                    } else {
                        this.slotInfo.getFluidStackList().set(index, FluidStack.EMPTY);
                    }
                }
            }
        } else {

            if (this.slotInfo.isFluid()) {
                ItemStack stack = this.slotInfo.get(index);
                if (FluidHandlerFix.hasFluidHandler(stack)) {
                    this.slotInfo.getFluidStackList().set(index, new FluidStack(FluidHandlerFix.getFluidHandler(stack).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE), 1));
                } else {
                    this.slotInfo.getFluidStackList().set(index, FluidStack.EMPTY);
                }

            }
            set(itemstack12);
        }
        this.setChanged();
    }
}
