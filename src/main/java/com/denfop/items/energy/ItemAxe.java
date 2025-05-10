package com.denfop.items.energy;

import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;

public class ItemAxe extends AxeItem {
    private final String name;
    private String nameItem;

    public ItemAxe(String name) {
        super(IUTiers.RUBY, 5.0F, -3.0F,new Properties().tab(IUCore.EnergyTab).stacksTo(1));
        this.name=name;
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
            this.nameItem = "item."+name;
        }

        return this.nameItem;
    }

}
