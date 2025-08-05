package com.denfop.items.energy;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EntityAdvArrow extends Arrow {
    private ItemStack stack;
    private ItemStack stack1;
    private int k;

    public EntityAdvArrow(Level p_36866_, LivingEntity p_345746_, ItemStack p_309200_, @Nullable ItemStack p_345461_) {
        super(p_36866_, p_345746_, p_309200_, p_345461_);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    protected void setPickupItemStack(ItemStack p_331667_) {

    }

    protected void doKnockback(LivingEntity p_346111_, DamageSource p_346412_) {
        double d0 = (double) k;
        if (d0 > 0.0) {
            double d1 = Math.max(0.0, 1.0 - p_346111_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(d0 * 0.6 * d1);
            if (vec3.lengthSqr() > 0.0) {
                p_346111_.push(vec3.x, 0.1, vec3.z);
            }
        }

    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void setKsnockback(int k) {
        this.k = k;
    }
}
