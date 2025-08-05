package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockSpace3;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockSpace3 extends ItemBlockCore<BlockSpace3.Type> implements IItemTag {
    public ItemBlockSpace3(BlockCore p_40565_, BlockSpace3.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        String name = getElement().getName().split("_")[1];
        return new String[]{"c:ores/" + name.replace("vanadium", "vanady"), "c:ores"};
    }
}
