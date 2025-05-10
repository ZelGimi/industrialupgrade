package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockNuclearBomb;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockNuclearBomb extends ItemBlockCore<BlockNuclearBomb.Type> {
    public ItemBlockNuclearBomb(BlockCore p_40565_, BlockNuclearBomb.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.ReactorsTab));
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="block.industrialupgrade.nuclear_bomb";
        }

        return this.nameItem;
    }
}
