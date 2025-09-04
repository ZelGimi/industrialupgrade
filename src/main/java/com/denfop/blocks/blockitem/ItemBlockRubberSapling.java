package com.denfop.blocks.blockitem;

import com.denfop.IUCore;
import com.denfop.tabs.IItemTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockRubberSapling extends BlockItem implements IItemTab {
    private String nameItem;

    public ItemBlockRubberSapling(Block p_40565_) {
        super(p_40565_, (new Item.Properties()));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.IUTab;
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.sapling";
        }

        return this.nameItem;
    }
}
