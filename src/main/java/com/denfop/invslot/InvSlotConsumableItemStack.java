package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.core.util.ItemComparableItemStack;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableItemStack extends InvSlotConsumable {

    private final Set<ItemComparableItemStack> stacks;

    public InvSlotConsumableItemStack(IInventorySlotHolder<?> base1, String name1, int count, ItemStack... stacks) {
        this(base1, name1, Access.I, count, InvSide.TOP, stacks);
    }

    public InvSlotConsumableItemStack(
            IInventorySlotHolder<?> base1,
            String name1,
            InvSlot.Access access1,
            int count,
            InvSlot.InvSide preferredSide1,
            ItemStack... stacks
    ) {
        super(base1, name1, access1, count, preferredSide1);
        this.stacks = new HashSet<>();

        for (ItemStack stack : stacks) {
            this.stacks.add(new ItemComparableItemStack(stack, true));
        }

    }

    public boolean accepts(ItemStack stack, int index) {
        return this.stacks.contains(new ItemComparableItemStack(stack, false));
    }

}
