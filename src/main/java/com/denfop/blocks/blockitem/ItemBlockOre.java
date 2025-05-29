package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemBlockOre extends ItemBlockCore<BlockOre.Type> implements IItemTag {
    public ItemBlockOre(BlockCore p_40565_, BlockOre.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade."+getElement().getMainPath()+".";
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
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        String name =  getElement().getName();
        switch (this.getElement().getId()) {
            case 3:
                 name = "tungsten";
                break;
            case 2:
                name = "vanady";
                break;

        }
        return new String[]{"forge:ores/" +name, "forge:ores"};
    }
}
