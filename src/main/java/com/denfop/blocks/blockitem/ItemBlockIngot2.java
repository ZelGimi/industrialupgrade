package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockIngots2;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockIngot2 extends ItemBlockCore<BlockIngots2.Type> implements IItemTag {
    public ItemBlockIngot2(BlockCore p_40565_, BlockIngots2.Type element) {
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
        String name = getElement().getName();
        return new String[]{"forge:storage_blocks/" + name.replace("_", ""), "forge:storage_blocks"};
    }
}
