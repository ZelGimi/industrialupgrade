package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockGas;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockGas extends ItemBlockCore<BlockGas.Type> {
    public ItemBlockGas(BlockCore p_40565_, BlockGas.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.OreTab));
    }


}
