package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.datagen.itemtag.IItemTag;
import net.minecraft.world.item.Item;

public class ItemBlockClassicOre extends ItemBlockCore<BlockClassicOre.Type> implements IItemTag {
    public ItemBlockClassicOre(BlockCore p_40565_, BlockClassicOre.Type element) {
        super(p_40565_, element, new Properties(), IUCore.OreTab);
    }

    @Override
    public Item getItem() {
        return this;
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu."+this.getElement().getName()+"_ore.name";
        }

        return "" + this.nameItem;
    }

    @Override
    public String[] getTags() {
        return new String[]{"forge:ores/" + getElement().getName().toLowerCase(), "forge:ores"};
    }
}
