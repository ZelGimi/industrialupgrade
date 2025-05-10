package com.denfop.items;

import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class IUItemBase extends Item {
    private String nameItem;

    public IUItemBase() {
        super(new Properties().tab(IUCore.ItemTab));
    }

    public IUItemBase(CreativeModeTab tabCore) {
        super(new Properties().tab(tabCore));
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
}
