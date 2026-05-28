package com.denfop.containermenu;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.inventory.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotVirtualMonitor extends Slot {
    private final VirtualSlot slotInfo;
    private final int index;
    private final BlockEntityPatternMonitor patternBlock;

    public SlotVirtualMonitor(final BlockEntityPatternMonitor inventoryIn,
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
        this.patternBlock = inventoryIn;
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

            if (this.patternBlock.modeCraft == 0)
                stack.setCount(1);
            if (this.mayPlace(stack)) {
                set(stack);
            }
        } else {
            if (this.patternBlock.modeCraft == 0 && this.container == patternBlock.outputItems)
                return;

            set(itemstack12);
        }
        this.setChanged();
    }

    public void setFluid(SameStack stack) {
        slotInfo.setFluid(this.index, stack);
        this.setChanged();
    }
}
