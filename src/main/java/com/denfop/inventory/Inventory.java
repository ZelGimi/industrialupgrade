package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class Inventory extends AbstractList<ItemStack> implements ITypeSlot, Container {

    public CustomWorldContainer base;
    protected TypeItemSlot typeItemSlot;
    protected NonNullList<ItemStack> contents;
    protected int stackSizeLimit;


    public Inventory(CustomWorldContainer base, TypeItemSlot typeItemSlot, int count) {

        this.contents = NonNullList.withSize(count, ItemStack.EMPTY);
        this.base = (CustomWorldContainer) base;
        this.typeItemSlot = typeItemSlot;
        this.stackSizeLimit = 64;
        base.addInventorySlot(this);

    }

    public Inventory(int count) {
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

    @Override
    public ItemStack getItem(int pSlot) {
        return this.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int amount) {
        ItemStack stack = this.getItem(pSlot);
        if (ModUtils.isEmpty(stack)) {
            return ModUtils.emptyStack;
        } else if (amount >= ModUtils.getSize(stack)) {
            this.set(pSlot, ModUtils.emptyStack);
            return stack;
        } else {
            if (amount != 0) {
                if (amount < 0) {
                    int space = Math.min(
                            getStackSizeLimit(),
                            stack.getMaxStackSize()
                    ) - ModUtils.getSize(stack);
                    amount = Math.max(amount, -space);
                }

                this.set(pSlot, ModUtils.decSize(stack, amount));
            }

            ItemStack ret = stack.copy();
            ret = ModUtils.setSize(ret, amount);
            return ret;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = get(pSlot);
        this.contents.set(pSlot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        set(pSlot, pStack);
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.clear();
    }

    public void readFromNbt(HolderLookup.Provider p_332027_, CompoundTag nbt) {
        readFromNbt(nbt, p_332027_);
    }

    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider p_332027_) {
        this.clear();
        ContainerHelper.loadAllItems(nbt, this.contents, p_332027_);
        this.setChanged();
    }

    public CompoundTag writeToNbt(HolderLookup.Provider p_332027_, CompoundTag nbt) {
        return writeToNbt(nbt, p_332027_);
    }

    public CompoundTag writeToNbt(CompoundTag nbt, HolderLookup.Provider p_332027_) {
        ContainerHelper.saveAllItems(nbt, this.contents, p_332027_);
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
                            if (stack.getComponents().isEmpty() && stack1.getComponents().isEmpty()) {
                                if (!simulate) {
                                    stack1.grow(stack.getCount());
                                }
                                return 0;
                            } else {
                                if (ModUtils.checkNbtEquality(stack.getComponents(), this.get(i).getComponents())) {
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
                        this.set(minSlot, ModUtils.setSize(stack, count));
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
                                if (stack.getComponents().isEmpty() && this.get(i).getComponents().isEmpty()) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (ModUtils.checkNbtEquality(stack.getComponents(), this.get(i).getComponents())) {
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

    @Override
    public int getContainerSize() {
        return this.size();
    }

    public void setChanged() {
    }

    public boolean canPlaceItem(final int index, ItemStack stack) {
        return true;
    }

    public boolean canInput() {
        return this.typeItemSlot == TypeItemSlot.INPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    public boolean canOutput() {
        return this.typeItemSlot == TypeItemSlot.OUTPUT || this.typeItemSlot == TypeItemSlot.INPUT_OUTPUT;
    }

    @Override
    public int getMaxStackSize() {
        return this.stackSizeLimit;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public ItemStack set(int i, ItemStack empty) {
        this.contents.set(i, empty);
        this.setChanged();
        return empty;
    }

    public boolean canShift() {
        return true;
    }

    public void update() {
    }

    public void read(Inventory inventory) {
        for (int i = 0; i < this.size(); i++) {
            this.set(i, inventory.get(i));
        }
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
