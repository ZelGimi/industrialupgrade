package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InvSlotConsumableId extends InvSlotConsumable {

    private final Set<Item> items;

    public InvSlotConsumableId(IInventorySlotHolder<?> base1, String name1, int count, Item... items) {
        this(base1, name1, Access.I, count, InvSide.TOP, items);
    }

    public InvSlotConsumableId(
            IInventorySlotHolder<?> base1,
            String name1,
            Access access1,
            int count,
            InvSide preferredSide1,
            Item... items
    ) {
        super(base1, name1, access1, count, preferredSide1);
        this.items = new HashSet<>();
        this.items.addAll(Arrays.asList(items));
    }

    public boolean accepts(ItemStack stack, int i) {
        return this.items.contains(stack.getItem());
    }

}
