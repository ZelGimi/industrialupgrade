package com.denfop.api.recipe;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.invslot.Inventory;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InventoryOutput extends Inventory {


    public InventoryOutput(IAdvInventory<?> base1, int count) {
        super(base1, TypeItemSlot.OUTPUT, count);
    }

    public boolean isItemValidForSlot(final int index, ItemStack stack) {
        return false;
    }

    public boolean addAll(List<ItemStack> stacks) {
        return this.addAll(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addAll(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(List<ItemStack> stacks) {
        boolean can = true;
        for (ItemStack stack : stacks) {
            can = can && this.canAdd(stack);
        }
        return can;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addAll(Collections.singletonList(stack), true);
        }
    }

    public boolean addAll(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {
            for (ItemStack stack : stacks) {

                int minSlot = this.size();
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (i < minSlot) {
                            minSlot = i;
                        }
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (ModUtils.checkNbtEquality(stack.getTagCompound(), this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (minSlot != this.size()) {
                    if (!simulate) {
                        this.put(minSlot, stack.copy());

                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public boolean addWithoutIgnoring(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {
            LinkedList<Integer> linkedList = new LinkedList<>();
            int col = 0;
            cycle:
            for (ItemStack stack : stacks) {

                int minSlot = this.size();
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (i < minSlot && !linkedList.contains(i)) {
                            minSlot = i;
                        }
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    col++;
                                    linkedList.add(i);
                                    continue cycle;
                                } else {
                                    if (ModUtils.checkNbtEquality(stack.getTagCompound(), this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        col++;
                                        linkedList.add(i);
                                        continue cycle;
                                    }
                                }
                            }
                        }
                    }
                }
                if (minSlot != this.size()) {
                    if (!simulate) {
                        this.put(minSlot, stack.copy());

                    }
                    col++;
                    linkedList.add(minSlot);
                    continue cycle;
                }
            }

            return col == stacks.size();
        }
        return true;
    }

    public boolean addAll(List<ItemStack> stacks, int size) {

        if (stacks != null && !stacks.isEmpty()) {
            for (ItemStack stack : stacks) {

                int minSlot = this.size();
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (i < minSlot) {
                            minSlot = i;
                        }
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() * size <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    this.get(i).grow(stack.getCount() * size);
                                    return true;
                                } else {
                                    if (ModUtils.checkNbtEquality(stack.getTagCompound(), this.get(i).getTagCompound())) {
                                        this.get(i).grow(stack.getCount() * size);

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (minSlot != this.size()) {
                    stack = stack.copy();
                    stack.setCount(stack.getCount() * size);
                    this.put(minSlot, stack);
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void addAll(ItemStack stack, int size) {
        int count = size * stack.getCount();
        for (int i = 0; i < this.size(); i++) {
            ItemStack slot = this.get(i);
            if (slot.isEmpty()) {
                final int size1 = Math.min(count, stack.getMaxStackSize());
                count -= size1;
                slot = stack.copy();
                slot.setCount(size1);
                this.put(i, slot);
                if (count == 0) {
                    return;
                }
            } else {
                if (slot.isItemEqual(stack)) {
                    if (slot.getCount() == stack.getMaxStackSize()) {
                        continue;
                    }
                    if (stack.getTagCompound() == null && slot.getTagCompound() == null) {
                        final int size1 = Math.min(count, stack.getMaxStackSize() - slot.getCount());
                        count -= size1;
                        this.get(i).grow(size1);
                        if (count == 0) {
                            return;
                        }
                    } else {
                        if (ModUtils.checkNbtEquality(stack.getTagCompound(), slot.getTagCompound())) {
                            final int size1 = Math.min(count, stack.getMaxStackSize() - slot.getCount());
                            count -= size1;
                            this.get(i).grow(size1);
                            if (count == 0) {
                                return;
                            }
                        }
                    }

                }
            }
        }
    }

}
