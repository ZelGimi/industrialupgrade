package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.mechanism.BlockEntityWaterRotorModifier;
import com.denfop.items.ItemWaterRotor;
import net.minecraft.world.item.ItemStack;

public class InventoryRotorWater extends Inventory implements ITypeSlot {

    private final InventoryWaterUpgrade slotUpgrade;

    public InventoryRotorWater(InventoryWaterUpgrade slotUpgrade) {
        super(slotUpgrade.base, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(1);
        this.slotUpgrade = slotUpgrade;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.WATER_ROTOR;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return stack.getItem() instanceof ItemWaterRotor;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        if (content.isEmpty()) {
            if (!this.contents.get(index).isEmpty()) {
                ((BlockEntityWaterRotorModifier) this.slotUpgrade.base).updateTileServer(null, 0);
                this.slotUpgrade.update(((BlockEntityWaterRotorModifier) this.slotUpgrade.base).rotor_slot.get(0));
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
