package com.denfop.blocks.blockitem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockLeaves extends BlockItem {
    private String nameItem;

    public ItemBlockLeaves(Block p_40565_) {
        super(p_40565_, (new Item.Properties()));
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem ="iu.leaves.rubber";
        }

        return this.nameItem;
    }
}
