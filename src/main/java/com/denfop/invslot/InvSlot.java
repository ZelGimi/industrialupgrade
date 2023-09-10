package com.denfop.invslot;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvSlot {

    public final IAdvInventory<?> base;
    protected final TypeItemSlot typeItemSlot;
    protected final List<ItemStack> contents;
    protected int stackSizeLimit;


    public InvSlot(IAdvInventory<?> base, TypeItemSlot typeItemSlot, int count) {

        this.contents = new ArrayList<>(Collections.nCopies(count, ItemStack.EMPTY));
        this.base = base;
        this.typeItemSlot = typeItemSlot;
        this.stackSizeLimit = 64;
        base.addInventorySlot(this);

    }

    public InvSlot(int count) {
        this.contents = new ArrayList<>(Collections.nCopies(count, ItemStack.EMPTY));
        this.base = null;
        this.typeItemSlot = null;
    }

    public ItemStack[] gets() {
        return this.contents.toArray(new ItemStack[0]);
    }

    public void readFromNbt(NBTTagCompound nbt) {
        NBTTagList contentsTag = nbt.getTagList("Items", 10);

        for (int i = 0; i < contentsTag.tagCount(); ++i) {
            NBTTagCompound contentTag = contentsTag.getCompoundTagAt(i);
            if (i < this.size()) {
                ItemStack stack = new ItemStack(contentTag);
                if (!ModUtils.isEmpty(stack)) {
                    this.putFromNBT(i, stack);
                }
            }
        }

        this.onChanged();
    }

    public boolean add(List<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
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
            return this.add(Collections.singletonList(stack), true);
        }
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {

        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
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
            }
            return false;
        }
        return true;
    }

    public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
        NBTTagList contentsTag = new NBTTagList();

        for (ItemStack content : this.contents) {
            NBTTagCompound contentTag = new NBTTagCompound();
            content.writeToNBT(contentTag);
            contentsTag.appendTag(contentTag);
        }

        nbt.setTag("Items", contentsTag);
        return nbt;
    }

    public int size() {
        return this.contents.size();
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.contents) {
            if (!ModUtils.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    public boolean isEmpty(int index) {
        return ModUtils.isEmpty(this.contents.get(index));
    }

    public ItemStack get() {
        return this.get(0);
    }

    public ItemStack get(int index) {
        return this.contents.get(index);
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    protected void putFromNBT(int index, ItemStack content) {
        this.contents.set(index, content);
    }

    public void put(int index, ItemStack content) {
        this.contents.set(index, content);
        this.onChanged();
    }


    public void clear(int index) {
        this.put(index, ItemStack.EMPTY);
    }

    public void onChanged() {
    }

    public boolean accepts(ItemStack stack, final int index) {
        return true;
    }

    public boolean canInput() {
        return this.typeItemSlot == TypeItemSlot.INPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    public boolean canOutput() {
        return this.typeItemSlot == TypeItemSlot.OUTPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


    public enum TypeItemSlot {
        INPUT,
        OUTPUT,
        INPUT_OUTPUT;

        TypeItemSlot() {
        }

        public boolean isInput() {
            return this == INPUT || this == INPUT_OUTPUT;
        }

        public boolean isOutput() {
            return this == OUTPUT || this == INPUT_OUTPUT;
        }
    }

}
