package com.denfop.invslot;

import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.IOutputItem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.item.ItemStack;

public class InvSlotOutputMiniPanels extends InvSlot {

    private final TileEntityMiniPanels tile;

    public InvSlotOutputMiniPanels(final TileEntityMiniPanels base) {
        super(base, TypeItemSlot.INPUT, 4);
        this.tile = base;

        setStackSizeLimit(1);
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof IOutputItem;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        SolarEnergySystem.system.recalculation(this.tile, EnumTypeParts.OUTPUT);
    }

}
