package com.denfop.invslot;

import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.IBatteryItem;
import com.denfop.api.solar.SolarEnergySystem;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.world.item.ItemStack;

public class InvSlotStorageMiniPanels extends InvSlot {

    private final TileEntityMiniPanels tile;

    public InvSlotStorageMiniPanels(final TileEntityMiniPanels base) {
        super(base, TypeItemSlot.INPUT, 4);
        this.tile = base;

        setStackSizeLimit(1);
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return stack.getItem() instanceof IBatteryItem;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        SolarEnergySystem.system.recalculation(this.tile, EnumTypeParts.CAPACITY);
        return content;
    }

}
