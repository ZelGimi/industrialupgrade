package com.denfop.items.energy;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ItemToolIU extends DiggerItem implements IItemTab, IItemTag {
    String nameItem;

    public ItemToolIU(TagKey<Block> p_204111_) {
        super(Tiers.IRON, p_204111_, new Properties().stacksTo(1).setNoRepair().attributes(DiggerItem.createAttributes(Tiers.IRON, Tiers.IRON.getAttackDamageBonus(), Tiers.IRON.getSpeed())));
        ItemTagProvider.list.add(this);
        ;
    }

    public ItemToolIU(TagKey<Block> p_204111_, Properties properties) {
        super(Tiers.IRON, p_204111_, properties);
    }

    public ItemToolIU(TagKey<Block> p_204111_, Properties properties, Tier tiers) {
        super(tiers, p_204111_, properties.attributes(DiggerItem.createAttributes(tiers, tiers.getAttackDamageBonus(), tiers.getSpeed())));
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);

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

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        return new String[0];
    }
}
