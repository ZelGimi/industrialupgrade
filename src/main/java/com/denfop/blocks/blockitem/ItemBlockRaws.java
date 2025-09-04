package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockRaws;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.Util;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemBlockRaws extends ItemBlockCore<BlockRaws.Type> implements IItemTag {
    public ItemBlockRaws(BlockCore p_40565_, BlockRaws.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        String name = getElement().getName();
        return new String[]{"forge:storage_blocks/" + name, "forge:storage_blocks"};
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", ForgeRegistries.ITEMS.getKey(this)));
            String targetString = "industrialupgrade." + getElement().getMainPath() + ".";
            String replacement = "";
            int index = pathBuilder.indexOf(targetString);
            while (index != -1) {
                pathBuilder.replace(index, index + targetString.length(), replacement);
                index = pathBuilder.indexOf(targetString, index + replacement.length());
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem + ".name";
    }
}
