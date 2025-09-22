package com.denfop.api.windsystem.upgrade;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.windsystem.InventoryUpgrade;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemWindRotor;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import net.minecraft.item.ItemStack;

public class InventoryRotor extends Inventory implements ITypeSlot {

    private final InventoryUpgrade slotUpgrade;

    public InventoryRotor(InventoryUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setInventoryStackLimit(1);
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
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return stack.getItem() instanceof ItemWindRotor;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        if (content.isEmpty()) {
            if (!this.contents.get(index).isEmpty()) {
                ((TileEntityRotorModifier) this.slotUpgrade.base).updateTileServer(null, 0);
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
