package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.item.DamageItem;
import com.denfop.items.reactors.ItemDamage;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemChemistry extends ItemDamage implements DamageItem {
    private String nameItem;

    public ItemChemistry(int durability) {
        super(new Properties().tab(IUCore.ItemTab).setNoRepair(), durability);

    }


    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
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
    public int getMaxCustomDamage(ItemStack stack) {
        return 250;
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
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(p_41421_) - this.getCustomDamage(
                p_41421_)) + "/" + this.getMaxCustomDamage(p_41421_)));
    }

    public boolean isDamaged(@Nonnull ItemStack stack) {
        return (getDamage(stack) > 1);
    }
}
