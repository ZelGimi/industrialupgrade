package com.denfop.invslot;

import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.tiles.base.TileAntiUpgradeBlock;
import net.minecraft.item.ItemStack;

public class InvSlotAntiUpgradeBlock extends InvSlot {

    public InvSlotAntiUpgradeBlock(TileAntiUpgradeBlock tile) {
        super(tile, TypeItemSlot.INPUT, 1);
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        if (stack.getItem() instanceof IUpgradeItem) {
            return true;
        }
        return super.accepts(stack, index);
    }

}
