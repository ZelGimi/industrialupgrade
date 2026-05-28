package com.denfop.items.energy;

import net.minecraft.tags.BlockTags;

public class ItemNet extends ItemToolIU {
    public ItemNet() {
        super(BlockTags.BEEHIVES);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.net";
        }

        return this.nameItem;
    }
}
