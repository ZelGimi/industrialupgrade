package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockTexGlass;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockTexGlass extends ItemBlockCore<BlockTexGlass.Type> {
    public ItemBlockTexGlass(BlockCore p_40565_, BlockTexGlass.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="iu.glass";
        }

        return this.nameItem;
    }
}
