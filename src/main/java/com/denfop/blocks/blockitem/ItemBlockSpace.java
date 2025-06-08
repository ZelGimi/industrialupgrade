package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockSpace;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockSpace extends ItemBlockCore<BlockSpace.Type> implements IItemTag {
    public ItemBlockSpace(BlockCore p_40565_, BlockSpace.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }
    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        String name = getElement().getName().split("_")[1];
        return new String[]{"forge:ores/" + name, "forge:ores"};
    }
}
