package com.denfop.inventory;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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
        Arrays.fill(booleans, false);
        Arrays.fill(integers, 1);
        Arrays.fill(sameStacks, new SameStack());
        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);
        integerList = Arrays.asList(integers);
    }

    public void clear() {
        super.clear();
        SameStack[] sameStacks = new SameStack[sameStackList.size()];
        Boolean[] booleans = new Boolean[sameStackList.size()];
        Integer[] integers = new Integer[sameStackList.size()];
        Arrays.fill(booleans, false);
        Arrays.fill(integers, 1);
        Arrays.fill(sameStacks, new SameStack());
        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);
        integerList = Arrays.asList(integers);

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (!content.isEmpty() && !sameStackList.get(index).isEmpty() && sameStackList.get(index).isCorrect(content)) {
            content.setCount(sameStackList.get(index).getAmount(false));
        }
        sameStackList.set(index, new SameStack(content));
        if (!content.isEmpty() && booleanList.get(index)) {
            FluidStack fluidStack = FluidHandlerFix.getFluidHandler(content).getFluidInTank(0);
            sameStackList.set(index, new SameStack(fluidStack));
        } else
            booleanList.set(index, false);
        return content;
    }

    @Override
    public CompoundTag writeToNbt(CompoundTag nbt) {
        CompoundTag tag = super.writeToNbt(nbt);

        ListTag listTag = new ListTag();

        for (Boolean value : booleanList) {
            listTag.add(ByteTag.valueOf((byte) (value ? 1 : 0)));
        }

        tag.put("BooleanList", listTag);
        listTag = new ListTag();

        for (Integer value : integerList) {
            listTag.add(IntTag.valueOf(value));
        }

        tag.put("IntegerList", listTag);
        listTag = new ListTag();

        for (SameStack value : sameStackList) {
            listTag.add(value.writeToNBT());
        }

        tag.put("SameStack", listTag);
        return tag;
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        super.readFromNbt(nbt);

        if (nbt.contains("BooleanList", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("BooleanList", Tag.TAG_BYTE);

            for (int i = 0; i < listTag.size(); i++) {
                booleanList.set(i, ((ByteTag) listTag.get(i)).getAsByte() != 0);
            }
        }
        if (nbt.contains("IntegerList", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("IntegerList", Tag.TAG_INT);

            for (int i = 0; i < listTag.size(); i++) {
                integerList.set(i, ((IntTag) listTag.get(i)).getAsInt());
            }
        }
        if (nbt.contains("SameStack", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("SameStack", Tag.TAG_BYTE);

            for (int i = 0; i < listTag.size(); i++) {
                sameStackList.set(i, SameStack.readFromNBT(listTag.getCompound(i)));
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
        sameStackList.set(index, stack);
        booleanList.set(index, true);
    }
}
