package com.denfop.containermenu;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.inventory.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotVirtualPreCraft extends Slot {
    private final VirtualSlot slotInfo;
    private final int index;
    private final BlockEntityPreCraft preCraft;

    public SlotVirtualPreCraft(final BlockEntityPreCraft inventoryIn,
                               final int index,
                               final int xPosition,
                               final int yPosition,
                               VirtualSlot slotInfo) {
        super(slotInfo instanceof Inventory ? ((Inventory) slotInfo) : inventoryIn,
                index,
                xPosition,
                yPosition);
        this.slotInfo = slotInfo;
        this.index = index;
        this.preCraft = inventoryIn;
    }

    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return this.slotInfo.canPlaceVirtualItem(this.index, itemStack);
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
        if (clickType == ClickType.CLONE)
            return;
        if (!itemstack12.isEmpty()) {

            ItemStack stack = itemstack12.copy();

            stack.setCount(1);
            if (this.mayPlace(stack)) {
                set(stack);
            }
        } else {
            set(itemstack12);
        }
        this.setChanged();
    }

    public void setFluid(SameStack stack) {
        slotInfo.setFluid(this.index, stack);
        this.setChanged();
    }
}
