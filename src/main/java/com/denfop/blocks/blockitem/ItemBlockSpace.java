package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockSpace;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockSpace extends ItemBlockCore<BlockSpace.Type> {
    public ItemBlockSpace(BlockCore p_40565_, BlockSpace.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.OreTab));
    }
}
