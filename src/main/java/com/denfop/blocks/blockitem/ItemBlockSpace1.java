package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockSpace1;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockSpace1 extends ItemBlockCore<BlockSpace1.Type> {
    public ItemBlockSpace1(BlockCore p_40565_, BlockSpace1.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.OreTab));
    }
}
