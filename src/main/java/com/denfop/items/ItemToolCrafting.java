package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemToolCrafting extends Item implements IItemTab {
    private String nameItem;

    public ItemToolCrafting(int damage) {
        super(new Properties().stacksTo(1).durability(damage - 1).setNoRepair());
    }

    protected static int getRemainingUses(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue() + 1;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int l = itemStack.getDamageValue() + 1;
        itemStack.setDamageValue(l);
        if (itemStack.getDamageValue() == itemStack.getMaxDamage())
            return ItemStack.EMPTY;
        return itemStack;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal(Localization.translate("item.itemTool.tooltip.UsesLeft", getRemainingUses(p_41421_))));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
