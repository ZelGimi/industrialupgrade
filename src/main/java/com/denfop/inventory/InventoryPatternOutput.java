package com.denfop.inventory;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventoryPatternOutput extends Inventory implements VirtualSlot {

    public List<SameStack> sameStackList;
    public List<Boolean> booleanList;

    public InventoryPatternOutput(BlockEntityPatternMonitor base, final int count) {
        super(base, TypeItemSlot.INPUT_OUTPUT, count);
        SameStack[] sameStacks = new SameStack[count];
        Boolean[] booleans = new Boolean[count];
        Arrays.fill(booleans, false);
        Arrays.fill(sameStacks, new SameStack());
        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);
    }

    public void clear() {
        super.clear();
        SameStack[] sameStacks = new SameStack[sameStackList.size()];
        Boolean[] booleans = new Boolean[sameStackList.size()];
        Arrays.fill(booleans, false);
        Arrays.fill(sameStacks, new SameStack());
        sameStackList = Arrays.asList(sameStacks);
        booleanList = Arrays.asList(booleans);

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        sameStackList.set(index, new SameStack(content));
        if (!content.isEmpty() && booleanList.get(index)) {
            FluidStack fluidStack = FluidHandlerFix.getFluidHandler(content).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            sameStackList.set(index, new SameStack(fluidStack));
        } else
            booleanList.set(index, false);
        return content;
    }


    @Override
    public CompoundTag writeToNbt(CompoundTag nbt, HolderLookup.Provider p_332027_) {
        CompoundTag tag = super.writeToNbt(nbt, p_332027_);

        ListTag listTag = new ListTag();

        for (Boolean value : booleanList) {
            listTag.add(ByteTag.valueOf((byte) (value ? 1 : 0)));
        }

        tag.put("BooleanList", listTag);
        listTag = new ListTag();

        for (SameStack value : sameStackList) {
            listTag.add(value.writeToNBT(p_332027_));
        }

        tag.put("SameStack", listTag);
        return tag;
    }

    @Override
    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider p_332027_) {
        super.readFromNbt(nbt, p_332027_);

        if (nbt.contains("BooleanList", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("BooleanList", Tag.TAG_BYTE);

            for (int i = 0; i < listTag.size(); i++) {
                booleanList.set(i, ((ByteTag) listTag.get(i)).getAsByte() != 0);
            }
        }
        if (nbt.contains("SameStack", Tag.TAG_LIST)) {
            ListTag listTag = nbt.getList("SameStack", Tag.TAG_BYTE);

            for (int i = 0; i < listTag.size(); i++) {
                sameStackList.set(i, SameStack.readFromNBT(listTag.getCompound(i), p_332027_));
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
