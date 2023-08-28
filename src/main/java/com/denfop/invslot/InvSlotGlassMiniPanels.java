package com.denfop.invslot;

import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarItem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.item.ItemStack;

public class InvSlotGlassMiniPanels extends InvSlot {

    private final TileEntityMiniPanels tile;

    public InvSlotGlassMiniPanels(final TileEntityMiniPanels base) {
        super(base, TypeItemSlot.INPUT, 9);
        this.tile = base;
        setStackSizeLimit(1);
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof ISolarItem;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        SolarEnergySystem.system.recalculation(this.tile, EnumTypeParts.GENERATION);
    }

}
