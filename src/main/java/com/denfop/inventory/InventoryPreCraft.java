package com.denfop.inventory;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventoryPreCraft extends Inventory implements VirtualSlot {

    public List<SameStack> sameStackList;
    public List<Boolean> booleanList;
    public List<Integer> integerList;

    public InventoryPreCraft(BlockEntityPreCraft base, final int count) {
        super(base, TypeItemSlot.INPUT_OUTPUT, count);

        SameStack[] sameStacks = new SameStack[count];
        Boolean[] booleans = new Boolean[count];
        Integer[] integers = new Integer[count];

        for (int i = 0; i < count; i++) {
            sameStacks[i] = new SameStack();
            booleans[i] = false;
            integers[i] = 1;
        }

        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);
        integerList = Arrays.asList(integers);
    }

    private static int safeAmount(Integer value) {
        if (value == null || value < 1) {
            return 1;
        }
        return value;
    }

    private static ItemStack normalizeSlotStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();



        copy.setCount(1);

        return copy;
    }

    private void sanitizeSlotStacks() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.get(i);

            if (!stack.isEmpty() && stack.getCount() != 1) {
                super.set(i, normalizeSlotStack(stack));
            }
        }
    }

    public void clear() {
        super.clear();

        SameStack[] sameStacks = new SameStack[sameStackList.size()];
        Boolean[] booleans = new Boolean[sameStackList.size()];
        Integer[] integers = new Integer[sameStackList.size()];

        for (int i = 0; i < sameStackList.size(); i++) {
            sameStacks[i] = new SameStack();
            booleans[i] = false;
            integers[i] = 1;
        }

        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);
        integerList = Arrays.asList(integers);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        ItemStack normalized = normalizeSlotStack(content);

        int amount = safeAmount(integerList.get(index));

        SameStack oldStack = sameStackList.get(index);
        if (!content.isEmpty() && oldStack != null && !oldStack.isEmpty() && oldStack.isCorrect(content)) {
            amount = safeAmount(oldStack.getAmount(false));
            integerList.set(index, amount);
        }

        super.set(index, normalized);

        if (normalized.isEmpty()) {
            sameStackList.set(index, new SameStack());
            booleanList.set(index, false);
            integerList.set(index, 1);
            return normalized;
        }

        if (booleanList.get(index)) {
            FluidStack fluidStack = FluidHandlerFix.getFluidHandler(normalized).getFluidInTank(0);
            SameStack fluidSameStack = new SameStack(fluidStack);
            fluidSameStack.setAmount(amount);
            sameStackList.set(index, fluidSameStack);
        } else {
            SameStack itemSameStack = new SameStack(normalized);
            itemSameStack.setAmount(amount);
            sameStackList.set(index, itemSameStack);
            booleanList.set(index, false);
        }

        return normalized;
    }

    @Override
    public CompoundTag writeToNbt(CompoundTag nbt, HolderLookup.Provider provider) {

        sanitizeSlotStacks();

        CompoundTag tag = super.writeToNbt(nbt, provider);

        ListTag booleanTag = new ListTag();
        for (Boolean value : booleanList) {
            booleanTag.add(ByteTag.valueOf((byte) (Boolean.TRUE.equals(value) ? 1 : 0)));
        }
        tag.put("BooleanList", booleanTag);

        ListTag integerTag = new ListTag();
        for (Integer value : integerList) {
            integerTag.add(IntTag.valueOf(safeAmount(value)));
        }
        tag.put("IntegerList", integerTag);

        ListTag sameStackTag = new ListTag();
        for (SameStack value : sameStackList) {
            if (value == null) {
                sameStackTag.add(new SameStack().writeToNBT(provider));
            } else {
                sameStackTag.add(value.writeToNBT(provider));
            }
        }
        tag.put("SameStack", sameStackTag);

        return tag;
    }

    @Override
    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider provider) {
        super.readFromNbt(nbt, provider);

        sanitizeSlotStacks();

        if (nbt.contains("BooleanList", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("BooleanList", Tag.TAG_BYTE);
            int max = Math.min(listTag.size(), booleanList.size());

            for (int i = 0; i < max; i++) {
                booleanList.set(i, ((ByteTag) listTag.get(i)).getAsByte() != 0);
            }
        }

        if (nbt.contains("IntegerList", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("IntegerList", Tag.TAG_INT);
            int max = Math.min(listTag.size(), integerList.size());

            for (int i = 0; i < max; i++) {
                integerList.set(i, safeAmount(((IntTag) listTag.get(i)).getAsInt()));
            }
        }

        if (nbt.contains("SameStack", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("SameStack", Tag.TAG_COMPOUND);
            int max = Math.min(listTag.size(), sameStackList.size());

            for (int i = 0; i < max; i++) {
                SameStack sameStack = SameStack.readFromNBT(listTag.getCompound(i), provider);
                sameStackList.set(i, sameStack == null ? new SameStack() : sameStack);
            }
        }
    }

    @Override
    public boolean isFluid() {
        return true;
    }

    @Override
    public List<FluidStack> getFluidStackList() {
        return Collections.emptyList();
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {

    }

    @Override
    public boolean canPlaceVirtualItem(int index, ItemStack stack) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public void setFluid(int index, SameStack stack) {
        if (stack == null || stack.isEmpty()) {
            sameStackList.set(index, new SameStack());
            booleanList.set(index, false);
            integerList.set(index, 1);
            return;
        }

        int amount = safeAmount(stack.getAmount(false));

        SameStack copy = stack.copyWithFluid();
        copy.setAmount(amount);

        sameStackList.set(index, copy);
        booleanList.set(index, true);
        integerList.set(index, amount);

        sanitizeSlotStacks();
    }
}