package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockRadiationOre extends ItemBlockCore<BlocksRadiationOre.Type> implements IItemTag {
    public ItemBlockRadiationOre(BlockCore p_40565_, BlocksRadiationOre.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.OreTab));
    }

    @Override
    public Item getItem() {
        return this;
    }


    @Override
    public String[] getTags() {
        return new String[]{"forge:ores/" + getElement().getName().split("_")[0], "forge:ores"};
    }
}
