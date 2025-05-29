package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockMineral extends ItemBlockCore<BlockMineral.Type> implements IItemTag {
    public ItemBlockMineral(BlockCore p_40565_, BlockMineral.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        return new String[]{"forge:ores/" + getElement().getName().toLowerCase(), "forge:ores"};
    }
}
