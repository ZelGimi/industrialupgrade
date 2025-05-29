package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockTropicalRubWood extends ItemBlockCore<BlockTropicalRubWood.RubberWoodState> {
    public ItemBlockTropicalRubWood(BlockCore p_40565_, BlockTropicalRubWood.RubberWoodState element) {
        super(p_40565_, element, new Properties(), IUCore.IUTab);
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="iu.tropical_rubber_wood";
        }

        return this.nameItem;
    }
}
