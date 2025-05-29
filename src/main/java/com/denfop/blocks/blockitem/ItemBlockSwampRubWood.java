package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockSwampRubWood extends ItemBlockCore<BlockSwampRubWood.RubberWoodState> {
    public ItemBlockSwampRubWood(BlockCore p_40565_, BlockSwampRubWood.RubberWoodState element) {
        super(p_40565_, element, new Properties(), IUCore.IUTab);
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="iu.swamp_rubber_wood";
        }

        return this.nameItem;
    }
}
