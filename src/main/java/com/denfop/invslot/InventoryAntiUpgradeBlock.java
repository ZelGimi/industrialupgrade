package com.denfop.invslot;

import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.tiles.base.TileAntiUpgradeBlock;
import net.minecraft.item.ItemStack;

public class InventoryAntiUpgradeBlock extends Inventory {

    public InventoryAntiUpgradeBlock(TileAntiUpgradeBlock tile) {
        super(tile, TypeItemSlot.INPUT, 1);
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        if (stack.getItem() instanceof IUpgradeItem) {
            return true;
        }
        return false;
    }

}
