package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.item.IDamageItem;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemChemistry extends Item implements IDamageItem, IItemTab {
    private String nameItem;

    public ItemChemistry(int durability) {
        super(new Properties().setNoRepair().durability(durability));

    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    public ItemChemistry(CreativeModeTab tabCore) {
        super(new Properties());
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

        return this.nameItem + ".name";
    }

    @Override
    public int getCustomDamage(ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        return nbt.getInt("advDmg");
    }

    @Override
    public int getMaxCustomDamage(ItemStack stack) {
        return 250;
    }

    @Override
    public boolean applyCustomDamage(ItemStack stack, int damage, LivingEntity src) {
        this.setCustomDamage(stack, this.getCustomDamage(stack) + damage);
        return true;
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
    public void setCustomDamage(ItemStack stack, int damage) {
        CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putInt("advDmg", damage);
        int maxStackDamage = stack.getMaxDamage();
        if (maxStackDamage > 2) {

            stack.setDamageValue(1 + (damage / 250 * ((maxStackDamage - 2))));
        }
    }

    public boolean isDamaged(@Nonnull ItemStack stack) {
        return (getDamage(stack) > 1);
    }
}
