package com.denfop.items.energy;

import net.minecraft.tags.BlockTags;

public class ItemNet extends ItemToolIU {
    public ItemNet() {
        super(0, 0, BlockTags.MINEABLE_WITH_AXE);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.net";
        }

        return this.nameItem;
    }
}
