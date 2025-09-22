package com.denfop.invslot;

import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.base.TileLimiter;
import net.minecraft.item.ItemStack;

public class InventoryLimiter extends Inventory {

    private final TileLimiter limiter;

    public InventoryLimiter(
            final TileLimiter base
    ) {
        super(base, TypeItemSlot.INPUT, 1);
        this.limiter = base;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
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
