package com.denfop.invslot;

import com.denfop.items.ItemAdvancedWaterRotor;
import net.minecraft.item.ItemStack;

public class InvSlotRotorWater extends InvSlot {

    private final InvSlotWaterUpgrade slotUpgrade;

    public InvSlotRotorWater(InvSlotWaterUpgrade slotUpgrade) {
        super(slotUpgrade.base, "rotor_slot", InvSlot.Access.I, 1, InvSide.ANY);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemAdvancedWaterRotor;
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
