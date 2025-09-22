package com.denfop.invslot;

import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.IOutputItem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.item.ItemStack;

public class InventoryOutputMiniPanels extends Inventory {

    private final TileEntityMiniPanels tile;

    public InventoryOutputMiniPanels(final TileEntityMiniPanels base) {
        super(base, TypeItemSlot.INPUT, 4);
        this.tile = base;

        setInventoryStackLimit(1);
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return stack.getItem() instanceof IOutputItem;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        SolarEnergySystem.system.recalculation(this.tile, EnumTypeParts.OUTPUT);
    }

}
