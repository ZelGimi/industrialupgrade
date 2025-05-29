package com.denfop.items.energy;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityAdvArrow extends Arrow {
    private ItemStack stack;
    private ItemStack stack1;

    public EntityAdvArrow(Level p_37569_, LivingEntity p_37570_) {
        super(p_37569_, p_37570_);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
