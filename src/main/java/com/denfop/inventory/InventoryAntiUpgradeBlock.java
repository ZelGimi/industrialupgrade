package com.denfop.inventory;

import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.blockentity.base.BlockEntityAntiUpgradeBlock;
import net.minecraft.world.item.ItemStack;

public class InventoryAntiUpgradeBlock extends Inventory {

    public InventoryAntiUpgradeBlock(BlockEntityAntiUpgradeBlock tile) {
        super(tile, TypeItemSlot.INPUT, 1);
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        if (stack.getItem() instanceof UpgradeItem) {
            return true;
        }
        return false;
    }

}
