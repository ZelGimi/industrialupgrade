package com.denfop.invslot;

import com.denfop.api.inv.IInvSlotProcessable;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public abstract class InvSlotProcessable extends InvSlot implements IInvSlotProcessable {

    public InvSlotProcessable(
            final IInventorySlotHolder<?> base,
            final String name,
            final int count
    ) {
        super(base, name, Access.I, count);
    }

    public void put(int index, ItemStack content) {
        super.put(index, content);

    }


//


}
