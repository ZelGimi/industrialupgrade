package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockFoam;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockFoam extends ItemBlockCore<BlockFoam.FoamType> {
    public ItemBlockFoam(BlockCore p_40565_, BlockFoam.FoamType element) {
        super(p_40565_, element, new Properties().tab(IUCore.IUTab));
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.foam";
        }

        return this.nameItem;
    }
}
