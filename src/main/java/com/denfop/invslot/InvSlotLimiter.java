package com.denfop.invslot;

import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.base.TileLimiter;
import net.minecraft.item.ItemStack;

public class InvSlotLimiter extends InvSlot {

    private final TileLimiter limiter;

    public InvSlotLimiter(
            final TileLimiter base
    ) {
        super(base, TypeItemSlot.INPUT, 1);
        this.limiter = base;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
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
