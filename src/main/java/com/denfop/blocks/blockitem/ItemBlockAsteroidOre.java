package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockAsteroidOre;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockAsteroidOre extends ItemBlockCore<BlockAsteroidOre.Type> implements IItemTag {
    public ItemBlockAsteroidOre(BlockCore p_40565_, BlockAsteroidOre.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        String name = getElement().getName();
        if (name.startsWith("deep_")) {
            name = name.substring("deep_".length());
        }
        if (name.startsWith("asteroid_")) {
            name = name.substring("asteroid_".length());
        }
        if (name.endsWith("_ore")) {
            name = name.substring(0, name.length() - "_ore".length());
        }
        return new String[]{"c:ores/" + name, "c:ores"};
    }
}
