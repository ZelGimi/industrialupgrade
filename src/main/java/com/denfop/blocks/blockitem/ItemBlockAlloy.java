package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlocksAlloy;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockAlloy extends ItemBlockCore<BlocksAlloy.Type> implements IItemTag {
    public ItemBlockAlloy(BlockCore p_40565_, BlocksAlloy.Type element) {
        super(p_40565_, element, new Properties(), IUCore.RecourseTab);
    }


    @Override
    public Item getItem() {
        return this;
    }


    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu."+this.getElement().getName()+"_block.name";
        }

        return "" + this.nameItem;
    }
    @Override
    public String[] getTags() {
        return new String[]{"forge:storage_blocks/" + getElement().getName().replace("_", "").replace("_", ""), "forge:storage_blocks"};
    }
}
