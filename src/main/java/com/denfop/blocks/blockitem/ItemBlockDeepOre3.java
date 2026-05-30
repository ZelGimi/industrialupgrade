package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockDeepOre3;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockDeepOre3 extends ItemBlockCore<BlockDeepOre3.Type> implements IItemTag {
    public ItemBlockDeepOre3(BlockCore p_40565_, BlockDeepOre3.Type element) {
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
        if (this.getElement() == BlockDeepOre3.Type.deep_potassium_phosphate_ore){
            name = "potassiumphosphate";
        }
        if (this.getElement() == BlockDeepOre3.Type.deep_sodium_phosphate_ore){
            name = "sodiumphosphate";
        }
        return new String[]{"forge:ores/" + name, "forge:ores"};
    }
}
