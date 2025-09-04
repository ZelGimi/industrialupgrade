package com.denfop.inventory;

import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.IOutputItem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import net.minecraft.world.item.ItemStack;

public class InventoryOutputMiniPanels extends Inventory {

    private final BlockEntityMiniPanels tile;

    public InventoryOutputMiniPanels(final BlockEntityMiniPanels base) {
        super(base, TypeItemSlot.INPUT, 4);
        this.tile = base;

        setStackSizeLimit(1);
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return stack.getItem() instanceof IOutputItem;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        SolarEnergySystem.system.recalculation(this.tile, EnumTypeParts.OUTPUT);
        return content;
    }

}
