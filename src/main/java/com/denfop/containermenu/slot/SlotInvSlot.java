package com.denfop.containermenu.slot;

import com.denfop.inventory.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotInvSlot extends Slot {
    public final Inventory inventory;
    public final int index;
    private int dragType;

    public SlotInvSlot(Inventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.inventory = inventory;
        this.index = index;
    }

    public ItemStack safeInsert(ItemStack itemstack11, int i3) {
        return super.safeInsert(itemstack11, i3);
    }

    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return this.inventory.canPlaceItem(this.index, itemStack);
    }

    public int getJeiX() {
        return this.x;
    }

    public int getJeiY() {
        return this.y;
    }

    public @NotNull ItemStack getItem() {
        return this.inventory.get(this.index);
    }

    @Override
    public void set(@NotNull ItemStack itemStack) {
        this.inventory.set(this.index, itemStack);

    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = this.inventory.get(this.index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return stack.split(amount);
    }


    @Override
    public boolean isSameInventory(Slot other) {
        if (other.container != this.inventory) {
            return false;
        }
        return this.index == other.index;
    }


    public int getMaxStackSize() {
        return this.inventory.getStackSizeLimit();
    }

    public void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {
        super.onTake(player, itemStack);

    }

    public void setDragType(int dragType) {
        this.dragType = dragType;
    }
}
