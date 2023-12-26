package com.denfop.invslot;

import com.denfop.items.ItemWaterRotor;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import net.minecraft.item.ItemStack;

public class InvSlotRotorWater extends InvSlot {

    private final InvSlotWaterUpgrade slotUpgrade;

    public InvSlotRotorWater(InvSlotWaterUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemWaterRotor;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        if(content.isEmpty()){
            if(!this.contents.get(index).isEmpty()){
                ((TileEntityWaterRotorModifier)this.slotUpgrade.base).updateTileServer(null,0);
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
