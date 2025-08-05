package com.denfop.items.reactors;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemReactorCoolant extends ItemDamage implements IReactorItem, IItemTab {

    private final int level;
    private final int heat;

    public ItemReactorCoolant(int level, int damage, int heat) {
        super(new Item.Properties().stacksTo(1), damage);
        this.level = level;
        this.heat = heat;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ReactorsTab;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(p_41421_) - this.getCustomDamage(
                p_41421_)) + "/" + this.getMaxCustomDamage(p_41421_)));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level") + this.level));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level1")));


    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
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
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.COOLANT_ROD;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getAutoRepair(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public int getRepairOther(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public int getDamageCFromHeat(final int heat, final IAdvReactor reactor) {
        return this.heat;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return 0.5;
    }

    @Override
    public void damageItem(final ItemStack stack, final int damage) {
        applyCustomDamage(stack, damage, null);
    }

    @Override
    public boolean updatableItem() {
        return true;
    }

    @Override
    public boolean caneExtractHeat() {
        return true;
    }

    @Override
    public double getEnergyProduction(final IAdvReactor reactor) {
        return 0;
    }

    public int needFill(ItemStack stack) {

        final int max = this.getMaxCustomDamage(stack);
        final int custom = this.getCustomDamage(
                stack);
        final int min = max / 1000;
        if (max - custom == max)
            return 0;
        return (max - custom) / min;
    }

    public void fill(ItemStack stack) {
        applyCustomDamage(stack, this.getMaxCustomDamage(stack) / 1000, null);
    }

}
