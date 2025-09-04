package com.denfop.items.reactors;

import com.denfop.api.item.DamageItem;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDamage extends Item implements DamageItem {


    private final int maxDamageItem;
    protected String nameItem;

    public ItemDamage(Item.Properties properties, int maxDamage) {
        super(properties);
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
        return !stack.hasTag() ? 0 : stack.getTag().getInt("damage");
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
        if (!stack.hasTag()) {
            return 0;
        } else {
            assert stack.getTag() != null;
            return stack.getTag().getInt("damage");
        }
    }


    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDamageItem;
    }

    public void setDamage(@Nonnull ItemStack stack, int damage) {

    }

    public void setCustomDamage(ItemStack stack, int damage) {
        CompoundTag nbt = ModUtils.nbt(stack);
        if (damage > maxDamageItem) {
            damage = maxDamageItem;
        }
        nbt.putInt("damage", damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, LivingEntity src) {
        int damage1 = this.getCustomDamage(stack) + damage;
        if (damage1 <= 0)
            damage1 = 0;
        this.setCustomDamage(stack, damage1);
        return getBarWidth(stack) == 0;
    }


}
