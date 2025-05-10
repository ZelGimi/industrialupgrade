package com.denfop.api.windsystem.upgrade;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.windsystem.InvSlotUpgrade;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemWindRotor;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.world.item.ItemStack;

public class InvSlotRotor extends InvSlot implements ITypeSlot {

    private final InvSlotUpgrade slotUpgrade;

    public InvSlotRotor(InvSlotUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.ROTOR;
    }

    public boolean canShift() {
        return false;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
         return stack.getItem() instanceof ItemWindRotor;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        if (content.isEmpty()) {
            if (!this.contents.get(index).isEmpty()) {
                ((IUpdatableTileEvent) this.slotUpgrade.base).updateTileServer(null, 0);
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
