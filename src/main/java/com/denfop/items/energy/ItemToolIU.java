package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

public class ItemToolIU extends DiggerItem implements IItemTab {
    String nameItem;

    public ItemToolIU(float damage, float speed, TagKey<Block> p_204111_) {
        super(damage, speed, Tiers.IRON, p_204111_, new Properties().stacksTo(1).setNoRepair());
    }

    public ItemToolIU(float damage, float speed, TagKey<Block> p_204111_, Properties properties) {
        super(damage, speed, Tiers.IRON, p_204111_, properties);
    }

    public ItemToolIU(float damage, float speed, TagKey<Block> p_204111_, Properties properties, Tier tier) {
        super(damage, speed, tier, p_204111_, properties);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
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
}
