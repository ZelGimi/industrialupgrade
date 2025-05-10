package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockResource;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockResource extends ItemBlockCore<BlockResource.Type> implements IItemTag {
    public ItemBlockResource(BlockCore p_40565_, BlockResource.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        if (getElement().getId() == 2)
            return new String[]{};
        if (getElement().getId() < 1 || getElement().getId() > 6)
            return new String[]{};
        String name = getElement().getName();
        return new String[]{"forge:storage_blocks/" + name.replace("_block", ""), "forge:storage_blocks"};
    }
}
