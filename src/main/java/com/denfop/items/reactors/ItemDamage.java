package com.denfop.items.reactors;

import com.denfop.api.item.IDamageItem;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDamage extends Item implements IDamageItem {


    private final int maxDamageItem;
    protected String nameItem;

    public ItemDamage(Item.Properties properties, int maxDamage) {
        super(maxDamage == 0 ? properties.stacksTo(1) : properties.stacksTo(1).durability(maxDamage));
        this.maxDamageItem = maxDamage;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    public int getMaxDamage(ItemStack stack) {
        return getMaxCustomDamage(stack);
    }


    @Override
    public int getDamage(ItemStack stack) {
        if (!stack.has(DataComponents.DAMAGE))
            stack.set(DataComponents.DAMAGE, 0);
        return stack.get(DataComponents.DAMAGE);
    }

    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    public int getBarWidth(@Nonnull ItemStack stack) {
        return Math.round(13.0F - (float) ((double) this.getCustomDamage(stack) * 13.0F / (double) this.getMaxCustomDamage(stack)));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb((float) (Math.max(0.0F, 1.0F - (this.getCustomDamage(stack) / (double) this.getMaxCustomDamage(stack))) / 3.0F), 1.0F, 1.0F);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }


    public boolean isDamaged(@Nonnull ItemStack stack) {
        return this.getCustomDamage(stack) > 0;
    }


    public int getCustomDamage(ItemStack stack) {
        if (!stack.has(DataComponents.DAMAGE)) {
            return 0;
        } else {
            assert stack.get(DataComponents.DAMAGE) != null;
            return stack.get(DataComponents.DAMAGE);
        }
    }


    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDamageItem;
    }

    public void setDamage(@Nonnull ItemStack stack, int damage) {

    }

    public void setCustomDamage(ItemStack stack, int damage) {
        if (damage > maxDamageItem) {
            damage = maxDamageItem;
        }
        stack.set(DataComponents.DAMAGE, damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, LivingEntity src) {
        int damage1 = this.getCustomDamage(stack) + damage;
        if (damage1 <= 0)
            damage1 = 0;
        this.setCustomDamage(stack, damage1);
        return true;
    }


}
