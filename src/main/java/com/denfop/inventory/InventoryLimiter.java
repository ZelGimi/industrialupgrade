package com.denfop.inventory;


import com.denfop.blockentity.base.BlockEntityLimiter;
import com.denfop.items.ItemCraftingElements;
import net.minecraft.world.item.ItemStack;

public class InventoryLimiter extends Inventory {

    private final BlockEntityLimiter limiter;

    public InventoryLimiter(
            final BlockEntityLimiter base
    ) {
        super(base, TypeItemSlot.INPUT, 1);
        this.limiter = base;
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
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
