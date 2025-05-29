package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockRubWood extends ItemBlockCore<BlockRubWood.RubberWoodState> {
    public ItemBlockRubWood(BlockCore p_40565_, BlockRubWood.RubberWoodState element) {
        super(p_40565_, element, new Properties(), IUCore.IUTab);
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="iu.rubber_wood";
        }

        return this.nameItem;
    }
}
