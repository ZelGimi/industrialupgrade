package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlocksIngot;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockIngot extends ItemBlockCore<BlocksIngot.Type> implements IItemTag {
    public ItemBlockIngot(BlockCore p_40565_, BlocksIngot.Type element) {
        super(p_40565_, element, new Properties(), IUCore.RecourseTab);
    }

    @Override
    public Item getItem() {
        return this;
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu." + this.getElement().getName() + "_block.name";
        }

        return "" + this.nameItem;
    }

    @Override
    public String[] getTags() {
        String name = getElement().getName();


        return new String[]{"forge:storage_blocks/" + name.replace("_", ""), "forge:storage_blocks"};
    }
}
