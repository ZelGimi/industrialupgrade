package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockHumus;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockHumus extends ItemBlockCore<BlockHumus.Type> {
    public ItemBlockHumus(BlockCore p_40565_, BlockHumus.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.humus_block.name";
        }

        return this.nameItem;
    }
}
