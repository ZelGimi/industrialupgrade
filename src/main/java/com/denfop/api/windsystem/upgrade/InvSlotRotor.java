package com.denfop.api.windsystem.upgrade;

import com.denfop.api.windsystem.InvSlotUpgrade;
import com.denfop.items.ItemAdvancedWindRotor;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotRotor extends InvSlot {

    private final InvSlotUpgrade slotUpgrade;

    public InvSlotRotor(InvSlotUpgrade slotUpgrade) {
        super(slotUpgrade.base, "rotor_slot", InvSlot.Access.I, 1, InvSide.ANY);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public boolean accepts(final ItemStack stack) {
        return stack.getItem() instanceof ItemAdvancedWindRotor;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (content.isEmpty()) {
            this.slotUpgrade.update();
        }
        if (!content.isEmpty()) {
            this.slotUpgrade.update(content);
        }
    }

}
