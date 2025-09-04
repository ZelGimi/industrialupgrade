package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.PickaxeItem;

public class ItemPickaxe extends PickaxeItem implements IItemTab {
    private final String name;
    private String nameItem;

    public ItemPickaxe(String name) {
        super(IUTiers.RUBY, 1, -2.8F, new Properties().stacksTo(1));
        this.name = name;
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
}
