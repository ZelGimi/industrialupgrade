package com.denfop.api.windsystem.upgrade;

import com.denfop.api.windsystem.InvSlotUpgrade;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemWindRotor;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import net.minecraft.item.ItemStack;

public class InvSlotRotor extends InvSlot {

    private final InvSlotUpgrade slotUpgrade;

    public InvSlotRotor(InvSlotUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemWindRotor;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        if(content.isEmpty()){
            if(!this.contents.get(index).isEmpty()){
                ((TileEntityRotorModifier)this.slotUpgrade.base).updateTileServer(null,0);
            }
        }
        super.put(index, content);
        if (content.isEmpty()) {
            this.slotUpgrade.update();
        }
        if (!content.isEmpty()) {
            this.slotUpgrade.update(content);
        }
    }

}
