package com.denfop.container;

import com.denfop.invslot.InvSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotInvSlot extends Slot {
    public final InvSlot invSlot;
    public final int index;
    private int dragType;

    public SlotInvSlot(InvSlot invSlot, int index, int xPosition, int yPosition) {
        super((Container) invSlot.base.getParent(), invSlot.base.getBaseIndex(invSlot) + index, xPosition, yPosition);
        this.invSlot = invSlot;
        this.index = index;
    }

    public ItemStack safeInsert(ItemStack itemstack11, int i3) {
        return super.safeInsert(itemstack11, i3);
    }

    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return this.invSlot.accepts(itemStack, this.index);
    }

    public int getJeiX() {
        return this.x;
    }

    public int getJeiY() {
        return this.y;
    }

    public @NotNull ItemStack getItem() {
        return this.invSlot.get(this.index);
    }

    @Override
    public void set(@NotNull ItemStack itemStack) {
        this.invSlot.set(this.index, itemStack);
        this.setChanged();
    }


    @Override
    public @NotNull ItemStack remove(int amount) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = this.invSlot.get(this.index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return stack.split(amount);
    }


    @Override
    public boolean isSameInventory(Slot other) {
        if (other.container != this.invSlot.base) {
            return false;
        }
        int baseIndex = this.invSlot.base.getBaseIndex(this.invSlot);
        if (baseIndex == -1) {
            return false;
        } else {
            return baseIndex + this.index == other.index;
        }
    }



    public int getMaxStackSize() {
        return this.invSlot.getStackSizeLimit();
    }

    public void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {
        super.onTake(player, itemStack);

    }

    public void setDragType(int dragType) {
        this.dragType = dragType;
    }
}
