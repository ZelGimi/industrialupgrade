package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemExchanger extends ItemDamage implements IExchangerItem {

    private final double percent;
    private final int level;

    public ItemExchanger(int level, double percent, int damage) {
        super(new Item.Properties().tab(IUCore.ReactorsTab).stacksTo(1), damage);
        this.percent = percent;
        this.level = level;

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
            this.nameItem = "iu.iu_" + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public double getPercent() {
        return percent;
    }

    public int getLevelExchanger() {
        return level;
    }


    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(p_41421_) - this.getCustomDamage(
                p_41421_)) + "/" + this.getMaxCustomDamage(p_41421_)));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level") + (this.level + 1)));
        p_41423_.add(Component.literal(Localization.translate("reactor.component_level1")));


    }

    @Override
    public boolean damageItem(final ItemStack stack, final int damage) {
        return applyCustomDamage(stack, damage, null);
    }

}
