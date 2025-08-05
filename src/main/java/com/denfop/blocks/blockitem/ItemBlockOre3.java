package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockOres3;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockOre3 extends ItemBlockCore<BlockOres3.Type> implements IItemTag {
    public ItemBlockOre3(BlockCore p_40565_, BlockOres3.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        return new String[]{"c:ores/" + getElement().getName(), "c:ores"};
    }

}
