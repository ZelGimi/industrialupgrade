package com.denfop.invslot;

import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class Inventory extends AbstractList<ItemStack> implements ITypeSlot, IInventory {

    public IAdvInventory<?> base;
    protected TypeItemSlot typeItemSlot;
    protected NonNullList<ItemStack> contents;
    protected int stackSizeLimit;


    public Inventory(IAdvInventory<?> base, TypeItemSlot typeItemSlot, int count) {

        this.contents = NonNullList.withSize(count, ItemStack.EMPTY);  this.base = base;
        this.typeItemSlot = typeItemSlot;
        this.stackSizeLimit = 64;
        base.addInventorySlot(this);

    }

    public Inventory(int count) {
        this.contents = NonNullList.withSize(count, ItemStack.EMPTY);   this.base = null;
        this.typeItemSlot = null;
    }

    public void clear() {

        this.contents =NonNullList.withSize(this.size(), ItemStack.EMPTY);
        this.stackSizeLimit = 64;
    }

    public boolean acceptAllOrIndex() {
        return true;
    }

    public TypeItemSlot getTypeItemSlot() {
        return typeItemSlot;
    }

    public void setTypeItemSlot(final TypeItemSlot typeItemSlot) {
        this.typeItemSlot = typeItemSlot;
    }


    public List<ItemStack> getContents() {
        return this.contents;
    }

    public void readFromNbt(NBTTagCompound nbt) {
        ItemStackHelper.loadAllItems(nbt,contents);
        this.markDirty();
    }

    public boolean addAll(List<ItemStack> stacks) {
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
            return this.addAll(Collections.singletonList(stack), false);
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
                            maxFill = Math.min(count, maxFill);
                            count -= maxFill;
                            stack1.grow(maxFill);
                        }
                    }
                }
            }
            if (count != 0) {
                if (minSlot != this.size()) {
                    if (!simulate) {
                        this.put(minSlot, ModUtils.setSize(stack,count));
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

    public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
        ItemStackHelper.saveAllItems(nbt,this.contents);
        return nbt;
    }

    public int size() {
        return this.contents.size();
    }

    @Override
    public int getSizeInventory() {
        return this.size();
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.contents) {
            if (!ModUtils.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        ItemStack stack = get(index);
        if (ModUtils.isEmpty(stack)) {
            return ModUtils.emptyStack;
        } else if (amount >= ModUtils.getSize(stack)) {
            this.set(index, ModUtils.emptyStack);
            return stack;
        } else {
            if (amount != 0) {
                if (amount < 0) {
                    int space = Math.min(
                            getInventoryStackLimit(),
                            stack.getMaxStackSize()
                    ) - ModUtils.getSize(stack);
                    amount = Math.max(amount, -space);
                }

                this.set(index, ModUtils.decSize(stack, amount));
            }

            ItemStack ret = stack.copy();
            ret = ModUtils.setSize(ret, amount);
            return ret;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(final int index) {
        ItemStack ret = get(index);
        if (!ModUtils.isEmpty(ret)) {
            this.set(index, ModUtils.emptyStack);
        }

        return ret;
    }

    @Override
    public void setInventorySlotContents(final int index, ItemStack stack) {
        if (ModUtils.isEmpty(stack)) {
            stack = ModUtils.emptyStack;
        }

        this.set(index, stack);
    }




    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(final EntityPlayer player) {

    }

    @Override
    public void closeInventory(final EntityPlayer player) {

    }



    @Override
    public int getField(final int id) {
        return 0;
    }

    @Override
    public void setField(final int id, final int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
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

    public void put(int index, ItemStack content) {
        this.contents.set(index, content);
        this.markDirty();
    }



    public void markDirty() {
    }

    public boolean isItemValidForSlot(final int index, ItemStack stack) {
        return true;
    }

    public boolean canInput() {
        return this.typeItemSlot == TypeItemSlot.INPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    public boolean canOutput() {
        return this.typeItemSlot == TypeItemSlot.OUTPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public ItemStack set(int i, ItemStack empty) {
        this.contents.set(i, empty);
        return  empty;
    }

    public boolean canShift() {
        return true;
    }

    public void update() {
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("");
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
