package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class IUItemBase extends Item implements IItemTab {
    private CreativeModeTab tabCore;
    private String nameItem;

    public IUItemBase() {
        super(new Properties());
    }

    public IUItemBase(CreativeModeTab tabCore) {
        super(new Properties());
        this.tabCore = tabCore;
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
    public CreativeModeTab getItemCategory() {
        return this.tabCore == null ? IUCore.ItemTab : this.tabCore;
    }
}
