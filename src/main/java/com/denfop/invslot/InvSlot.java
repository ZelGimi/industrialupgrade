package com.denfop.invslot;

import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvSlot implements ITypeSlot {

    public IAdvInventory<?> base;
    protected TypeItemSlot typeItemSlot;
    protected List<ItemStack> contents;
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

    public void reset(int size) {

        this.contents = new ArrayList<>(Collections.nCopies(size, ItemStack.EMPTY));
        this.stackSizeLimit = 64;
    }

    public boolean acceptAllOrIndex() {
        return true;
    }

    public TypeItemSlot getTypeItemSlot() {
        return typeItemSlot;
    }

    public ItemStack[] gets() {
        return this.contents.toArray(new ItemStack[0]);
    }

    public List<ItemStack> getContents() {
        return this.contents;
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
        boolean added = false;
        for (ItemStack stack : stacks) {
            added = added || this.add(stack);
        }
        return added;
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public int addExperimental(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.addExperimental(stack, false);
        }
    }

    public int addExperimental(ItemStack stack, boolean simulate) {

        if (stack != null && !stack.isEmpty()) {

            int count = stack.getCount();
            int minSlot = this.size();

            for (int i = 0; i < this.size(); i++) {
                final ItemStack stack1 = this.get(i);
                if (stack1.isEmpty()) {
                    if (i < minSlot) {
                        minSlot = i;
                    }
                } else {
                    if (stack1.isItemEqual(stack)) {
                        if (stack1.getCount() + count <= stack.getMaxStackSize()) {
                            if (stack.getTagCompound() == null && stack1.getTagCompound() == null) {
                                if (!simulate) {
                                    stack1.grow(stack.getCount());
                                }
                                return 0;
                            } else {
                                if (ModUtils.checkNbtEquality(stack.getTagCompound(), this.get(i).getTagCompound())) {
                                    if (!simulate) {
                                        stack1.grow(count);

                                    }
                                    return 0;
                                }
                            }
                        } else {
                            int maxFill = stack1.getMaxStackSize() - stack1.getCount();
                            if (maxFill == 0) {
                                continue;
                            }
                            maxFill = Math.max(count, maxFill);
                            count -= maxFill;
                            stack1.grow(stack.getCount());
                        }
                    }
                }
            }
            if (count != 0) {
                if (minSlot != this.size()) {
                    if (!simulate) {
                        this.put(minSlot, new ItemStack(stack.getItem(), count, stack.getItemDamage(), stack.getTagCompound()));
                    }
                    return 0;
                }
                return count;
            }
            return count;
        }
        return 0;

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

    public boolean add(List<ItemStack> stacks, boolean simulate) {

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
        return this.contents.get(index % this.size());
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

    public void setTypeItemSlot(final TypeItemSlot typeItemSlot) {
        this.typeItemSlot = typeItemSlot;
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

    public void set(int i, ItemStack empty) {
        this.contents.set(i, empty);
    }

    public boolean canShift() {
        return true;
    }

    public void update() {
    }


    public enum TypeItemSlot {
        INPUT,
        OUTPUT,
        INPUT_OUTPUT,
        NONE;

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
