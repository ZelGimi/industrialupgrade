package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.blocks.BlockCore;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.blocks.RadiationDustBlock;

public class ItemBlockRadiationDust extends ItemBlockCore<RadiationDustBlock.Type> {

    public ItemBlockRadiationDust(BlockCore block, RadiationDustBlock.Type element) {
        super(block, element, new Properties(), IUCore.OreTab);
    }


}