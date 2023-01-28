package com.denfop.invslot;

import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.base.TileEntityLimiter;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotLimiter extends InvSlot {

    private final TileEntityLimiter limiter;

    public InvSlotLimiter(
            final TileEntityLimiter base
    ) {
        super(base, "slot", Access.I, 1);
        this.limiter = base;
    }

    @Override
    public boolean accepts(final ItemStack stack) {
        return stack.getItem() instanceof ItemCraftingElements && stack.getItemDamage() >= 206 && stack.getItemDamage() <= 216;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (content.isEmpty()) {
            this.limiter.setTier(0);
        } else {
            this.limiter.setTier(content.getItemDamage() - 205);
        }
    }

}
