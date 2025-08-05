package com.denfop.items.energy;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;

import static net.minecraft.tags.ItemTags.PICKAXES;

public class ItemPickaxe extends PickaxeItem implements IItemTab, IItemTag {
    private final String name;
    private String nameItem;

    public ItemPickaxe(String name) {
        super(IUTiers.RUBY, new Properties().stacksTo(1).attributes(DiggerItem.createAttributes(IUTiers.RUBY, IUTiers.RUBY.getAttackDamageBonus(), IUTiers.RUBY.getSpeed())));
        this.name = name;
        ItemTagProvider.list.add(this);
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
            this.nameItem = "item." + name;
        }

        return this.nameItem;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        return new String[]{PICKAXES.location().toString()};
    }

}
