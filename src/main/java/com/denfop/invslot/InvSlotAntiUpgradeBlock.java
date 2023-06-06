package com.denfop.invslot;

import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.tiles.base.TileEntityAntiUpgradeBlock;
import net.minecraft.item.ItemStack;

public class InvSlotAntiUpgradeBlock extends InvSlot {

    public InvSlotAntiUpgradeBlock(TileEntityAntiUpgradeBlock tile) {
        super(tile, "input", InvSlot.Access.I, 1, InvSlot.InvSide.ANY);
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        if (stack.getItem() instanceof IUpgradeItem) {
            return true;
        }
        return super.accepts(stack, index);
    }

}
