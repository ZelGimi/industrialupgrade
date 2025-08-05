package com.denfop.invslot;

import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.ITileInventory;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class InvSlot extends AbstractList<ItemStack> implements ITypeSlot {

    public IAdvInventory<? extends ITileInventory> base;
    protected TypeItemSlot typeItemSlot;
    protected NonNullList<ItemStack> contents;
    protected int stackSizeLimit;


    public InvSlot(IAdvInventory<?> base, TypeItemSlot typeItemSlot, int count) {

        this.contents = NonNullList.withSize(count, ItemStack.EMPTY);
        this.base = (IAdvInventory<? extends ITileInventory>) base;
        this.typeItemSlot = typeItemSlot;
        this.stackSizeLimit = 64;
        base.addInventorySlot(this);

    }

    public InvSlot(int count) {
        this.contents = NonNullList.withSize(count, ItemStack.EMPTY);
        this.base = null;
        this.typeItemSlot = null;
    }

    public void clear() {
        this.contents = NonNullList.withSize(this.size(), ItemStack.EMPTY);
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

    public void readFromNbt(CompoundTag nbt) {
        this.clear();
        ContainerHelper.loadAllItems(nbt,this.contents);
        this.onChanged();
    }
    public CompoundTag writeToNbt(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, this.contents);
        return nbt;
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
                    if (stack1.getItem() == stack.getItem()) {
                        if (stack1.getCount() + count <= stack.getMaxStackSize()) {
                            if (stack.getTag() == null && stack1.getTag() == null) {
                                if (!simulate) {
                                    stack1.grow(stack.getCount());
                                }
                                return 0;
                            } else {
                                if (ModUtils.checkNbtEquality(stack.getTag(), this.get(i).getTag())) {
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
                        this.set(minSlot, ModUtils.setSize(stack,count));
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
                        if (this.get(i).getItem() == stack.getItem()) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTag() == null && this.get(i).getTag() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (ModUtils.checkNbtEquality(stack.getTag(), this.get(i).getTag())) {
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
                        this.set(minSlot, stack.copy());

                    }
                    return true;
                }
            }
            return false;
        }
        return true;
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



    public ItemStack get(int index) {
        return this.contents.get(index % this.size());
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

    public ItemStack set(int i, ItemStack empty) {
        this.contents.set(i, empty);
        this.onChanged();
        return empty;
    }

    public boolean canShift() {
        return true;
    }

    public void update() {
    }

    public boolean hasItemList() {
        return false;
    }

    public List<IInputItemStack> getStacks(int index) {
        return Collections.emptyList();
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
