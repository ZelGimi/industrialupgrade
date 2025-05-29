package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockOres2;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockOre2 extends ItemBlockCore<BlockOres2.Type> implements IItemTag {
    public ItemBlockOre2(BlockCore p_40565_, BlockOres2.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        return new String[]{"forge:ores/" + getElement().getName(), "forge:ores"};
    }

}
