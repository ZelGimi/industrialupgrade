package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.blocks.SootBlock;

public class ItemBlockSoot extends ItemBlockCore<SootBlock.Type> {

    public ItemBlockSoot(BlockCore block, SootBlock.Type element) {
        super(block, element, new Properties(), IUCore.OreTab);
    }


}