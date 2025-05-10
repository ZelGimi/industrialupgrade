package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.BlockOil;
import com.denfop.blocks.ItemBlockCore;

public class ItemBlockOil extends ItemBlockCore<BlockOil.Type> {
    public ItemBlockOil(BlockCore p_40565_, BlockOil.Type element) {
        super(p_40565_, element, new Properties().tab(IUCore.OreTab));
    }


}
