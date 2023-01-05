package com.denfop.invslot;

import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.tiles.base.TileEntityAntiUpgradeBlock;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotAntiUpgradeBlock extends InvSlot {

    public InvSlotAntiUpgradeBlock(TileEntityAntiUpgradeBlock tile) {
        super(tile, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
    }

    @Override
    public boolean accepts(final ItemStack stack) {
        if (stack.getItem() instanceof IUpgradeItem) {
            return true;
        }
        return super.accepts(stack);
    }

}
