package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.core.IC2;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class InvSlot implements Iterable<ItemStack> {

    public final IInventorySlotHolder<?> base;
    public final String name;
    public final InvSlot.InvSide preferredSide;
    protected final InvSlot.Access access;
    protected final ItemStack[] contents;
    protected int stackSizeLimit;

    public InvSlot(IInventorySlotHolder<?> base, String name, InvSlot.Access access, int count) {
        this(base, name, access, count, InvSlot.InvSide.ANY);
    }

    public InvSlot(IInventorySlotHolder<?> base, String name, InvSlot.Access access, int count, InvSlot.InvSide preferredSide) {
        if (count <= 0) {
            throw new IllegalArgumentException("invalid slot count: " + count);
        } else {
            this.contents = new ItemStack[count];
            this.clear();
            this.base = base;
            this.name = name;
            this.access = access;
            this.preferredSide = preferredSide;
            this.stackSizeLimit = 64;
            base.addInventorySlot(this);
        }
    }

    public InvSlot(int count) {
        this.contents = new ItemStack[count];
        this.clear();
        this.base = null;
        this.name = null;
        this.access = InvSlot.Access.NONE;
        this.preferredSide = InvSlot.InvSide.ANY;
    }

    public ItemStack[] gets() {
        return this.contents;
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.clear();
        NBTTagList contentsTag = nbt.getTagList("Contents", 10);

        for (int i = 0; i < contentsTag.tagCount(); ++i) {
            NBTTagCompound contentTag = contentsTag.getCompoundTagAt(i);
            int index = contentTag.getByte("Index") & 255;
            if (index >= this.size()) {
                IC2.log.error(
                        LogCategory.Block,
                        "Can't load item stack for %s, slot %s, index %d is out of bounds.",
                        Util.toString(this.base.getParent()), this.name, index
                );
            } else {
                ItemStack stack = new ItemStack(contentTag);
                if (StackUtil.isEmpty(stack)) {
                    IC2.log.warn(
                            LogCategory.Block,
                            "Can't load item stack %s for %s, slot %s, index %d, no matching item for %d:%d.",
                            StackUtil.toStringSafe(stack),
                            Util.toString(this.base.getParent()),
                            this.name,
                            index,
                            contentTag.getShort(
                                    "id"),
                            contentTag.getShort("Damage")
                    );
                } else {
                    if (!this.isEmpty(index)) {
                        IC2.log.error(
                                LogCategory.Block,
                                "Loading content to non-empty slot for %s, slot %s, index %d, replacing %s with %s.",
                                Util.toString(this.base.getParent()), this.name, index, this.get(index), stack
                        );
                    }

                    this.putFromNBT(index, stack);
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

    public void writeToNbt(NBTTagCompound nbt) {
        NBTTagList contentsTag = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i) {
            ItemStack content = this.contents[i];
            if (!StackUtil.isEmpty(content)) {
                NBTTagCompound contentTag = new NBTTagCompound();
                contentTag.setByte("Index", (byte) i);
                content.writeToNBT(contentTag);
                contentsTag.appendTag(contentTag);
            }
        }

        nbt.setTag("Contents", contentsTag);
    }

    public int size() {
        return this.contents.length;
    }

    public boolean isEmpty() {

        for (ItemStack stack : this.contents) {
            if (!StackUtil.isEmpty(stack)) {
                return false;
            }
        }

        return true;
    }

    public boolean isEmpty(int index) {
        return StackUtil.isEmpty(this.contents[index]);
    }

    public ItemStack get() {
        return this.get(0);
    }

    public ItemStack get(int index) {
        return this.contents[index];
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    protected void putFromNBT(int index, ItemStack content) {
        this.contents[index] = content;
    }

    public void put(int index, ItemStack content) {
        if (StackUtil.isEmpty(content)) {
            content = StackUtil.emptyStack;
        }

        this.contents[index] = content;
        this.onChanged();
    }

    public void clear() {
        Arrays.fill(this.contents, StackUtil.emptyStack);
    }

    public void clear(int index) {
        this.put(index, StackUtil.emptyStack);
    }

    public void onChanged() {
    }

    public boolean accepts(ItemStack stack, final int index) {
        return true;
    }

    public boolean canInput() {
        return this.access == InvSlot.Access.I || this.access == InvSlot.Access.IO;
    }

    public boolean canOutput() {
        return this.access == InvSlot.Access.O || this.access == InvSlot.Access.IO;
    }

    public void organize() {
        for (int dstIndex = 0; dstIndex < this.contents.length - 1; ++dstIndex) {
            ItemStack dst = this.contents[dstIndex];
            if (StackUtil.isEmpty(dst) || StackUtil.getSize(dst) < dst.getMaxStackSize()) {
                for (int srcIndex = dstIndex + 1; srcIndex < this.contents.length; ++srcIndex) {
                    ItemStack src = this.contents[srcIndex];
                    if (!StackUtil.isEmpty(src)) {
                        if (StackUtil.isEmpty(dst)) {
                            this.contents[srcIndex] = StackUtil.emptyStack;
                            dst = src;
                            this.contents[dstIndex] = src;
                        } else if (StackUtil.checkItemEqualityStrict(dst, src)) {
                            int space = Math.min(this.getStackSizeLimit(), dst.getMaxStackSize() - StackUtil.getSize(dst));
                            int srcSize = StackUtil.getSize(src);
                            if (srcSize > space) {
                                this.contents[srcIndex] = StackUtil.decSize(src, space);
                                this.contents[dstIndex] = StackUtil.incSize(dst, space);
                                break;
                            }

                            this.contents[srcIndex] = StackUtil.emptyStack;
                            this.contents[dstIndex] = dst = StackUtil.incSize(dst, srcSize);
                            if (srcSize == space) {
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public Iterator<ItemStack> iterator() {
        return new Iterator<ItemStack>() {
            private int idx = 0;

            public boolean hasNext() {
                return this.idx < InvSlot.this.contents.length;
            }

            public ItemStack next() {
                if (this.idx >= InvSlot.this.contents.length) {
                    throw new NoSuchElementException();
                } else {
                    return InvSlot.this.contents[this.idx++];
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String toString() {
        String ret = this.name + "[" + this.contents.length + "]: ";

        for (int i = 0; i < this.contents.length; ++i) {
            ret = ret + this.contents[i];
            if (i < this.contents.length - 1) {
                ret = ret + ", ";
            }
        }

        return ret;
    }

    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.contents.length];

        for (int i = 0; i < this.contents.length; ++i) {
            ItemStack content = this.contents[i];
            ret[i] = StackUtil.isEmpty(content) ? StackUtil.emptyStack : content.copy();
        }

        return ret;
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.contents.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.contents, 0, this.contents.length);

        }
    }

    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
    }

    public enum InvSide {
        ANY(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST),
        TOP(EnumFacing.UP),
        BOTTOM(EnumFacing.DOWN),
        SIDE(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST),
        NOTSIDE();

        private final Set<EnumFacing> acceptedSides;

        InvSide(EnumFacing... sides) {
            if (sides.length == 0) {
                this.acceptedSides = Collections.emptySet();
            } else {
                Set<EnumFacing> acceptedSides = EnumSet.noneOf(EnumFacing.class);
                acceptedSides.addAll(Arrays.asList(sides));
                this.acceptedSides = Collections.unmodifiableSet(acceptedSides);
            }

        }

        public boolean matches(EnumFacing side) {
            return this.acceptedSides.contains(side);
        }

        public Set<EnumFacing> getAcceptedSides() {
            return this.acceptedSides;
        }
    }

    public enum Access {
        NONE,
        I,
        O,
        IO;

        Access() {
        }

        public boolean isInput() {
            return (this.ordinal() & 1) != 0;
        }

        public boolean isOutput() {
            return (this.ordinal() & 2) != 0;
        }
    }

}
