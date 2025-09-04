package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockRubberSapling extends BlockItem {
    private String nameItem;

    public ItemBlockRubberSapling(Block p_40565_) {
        super(p_40565_, (new Item.Properties()).tab(IUCore.IUTab));
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.sapling";
        }

        return this.nameItem;
    }
}
