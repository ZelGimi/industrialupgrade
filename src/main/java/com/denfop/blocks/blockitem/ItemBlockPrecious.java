package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockPrecious;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockPrecious extends ItemBlockCore<BlockPrecious.Type> implements IItemTag {
    public ItemBlockPrecious(BlockCore p_40565_, BlockPrecious.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        String name = getElement().getName();
        return new String[]{"forge:storage_blocks/" + name.replace("_block", ""), "forge:storage_blocks"};
    }
}
