package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockDeposits extends ItemBlockCore<BlockDeposits.Type> {
    public ItemBlockDeposits(BlockCore p_40565_, BlockDeposits.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.RecourseTab));
    }
}
