package com.denfop.items.energy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAdvArrow extends EntityTippedArrow {

    private ItemStack stack;

    public EntityAdvArrow(final World p_i46777_1_) {
        super(p_i46777_1_);
    }

    public EntityAdvArrow(final World p_i46777_1_, EntityLivingBase p_i46777_2_) {
        super(p_i46777_1_, p_i46777_2_);
    }

    @Override
    protected ItemStack getArrowStack() {
        return super.getArrowStack();
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

}
