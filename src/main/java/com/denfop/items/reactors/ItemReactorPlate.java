package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemReactorPlate extends ItemDamage implements IReactorItem {

    private final int level;
    private final double percent;

    public ItemReactorPlate(int level, double percent) {
        super(new Item.Properties().tab(IUCore.ReactorsTab).stacksTo(1), 0);
        this.level = level;
        this.percent = percent;
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
            this.nameItem = "iu."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level") + this.level));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level1")));

    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.PLATE;
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
        return 1;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return percent;
    }

    @Override
    public void damageItem(final ItemStack stack, final int damage) {

    }

    @Override
    public boolean updatableItem() {
        return false;
    }

    @Override
    public boolean caneExtractHeat() {
        return false;
    }

    @Override
    public double getEnergyProduction(final IAdvReactor reactor) {
        return 0;
    }
}

