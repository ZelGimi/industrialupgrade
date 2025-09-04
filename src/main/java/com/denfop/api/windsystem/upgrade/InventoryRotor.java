package com.denfop.api.windsystem.upgrade;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.api.windsystem.InventoryUpgrade;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemWindRotor;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.world.item.ItemStack;

public class InventoryRotor extends Inventory implements ITypeSlot {

    private final InventoryUpgrade slotUpgrade;

    public InventoryRotor(InventoryUpgrade slotUpgrade) {
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
    public boolean canPlaceItem(final int index, final ItemStack stack) {
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
