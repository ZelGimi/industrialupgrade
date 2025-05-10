package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.ItemWaterRotor;
import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import net.minecraft.world.item.ItemStack;

public class InvSlotRotorWater extends InvSlot implements ITypeSlot {

    private final InvSlotWaterUpgrade slotUpgrade;

    public InvSlotRotorWater(InvSlotWaterUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.WATER_ROTOR;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ItemWaterRotor;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        if (content.isEmpty()) {
            if (!this.contents.get(index).isEmpty()) {
                ((TileEntityWaterRotorModifier) this.slotUpgrade.base).updateTileServer(null, 0);
            }
        }
        super.set(index, content);
        if (content.isEmpty()) {
            this.slotUpgrade.update();
        }
        if (!content.isEmpty()) {
            this.slotUpgrade.update(content);
        }
        return content;
    }

}
