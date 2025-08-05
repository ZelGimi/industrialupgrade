package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockHeavyOre extends ItemBlockCore<BlockHeavyOre.Type> implements IItemTag {
    public ItemBlockHeavyOre(BlockCore p_40565_, BlockHeavyOre.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        return new String[]{"c:ores/" + getElement().getName().toLowerCase(), "c:ores"};
    }
}
