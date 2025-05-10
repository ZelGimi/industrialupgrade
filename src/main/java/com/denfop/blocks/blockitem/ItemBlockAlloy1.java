package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlocksAlloy1;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockAlloy1 extends ItemBlockCore<BlocksAlloy1.Type> implements IItemTag {
    public ItemBlockAlloy1(BlockCore p_40565_, BlocksAlloy1.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
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
        String element = getElement().getName();
        if (this.getElement().getId() == 15)
            element = "galliumarsenic";
        return new String[]{"forge:storage_blocks/" + element.replace("_alloy","").replace("_", "").replace("_", ""), "forge:storage_blocks"};
    }
}
