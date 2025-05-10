package com.denfop.invslot;


import com.denfop.items.ItemCraftingElements;
import com.denfop.tiles.base.TileLimiter;
import net.minecraft.world.item.ItemStack;

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
        return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() >= 206 && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() <= 216;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (content.isEmpty()) {
            this.limiter.setTier(0);
        } else {
            this.limiter.setTier(((ItemCraftingElements<?>) content.getItem()).getElement().getId() - 205);
        }
        return content;
    }

}
