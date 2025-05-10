package com.denfop.items.reactors;

import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class ItemRadioactive extends Item {


    private String nameItem;

    public ItemRadioactive() {
        super(new Properties().tab(IUCore.ReactorsTab));
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
}
