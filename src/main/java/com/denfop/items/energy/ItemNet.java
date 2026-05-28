package com.denfop.items.energy;


import com.denfop.config.ModConfig;
import net.minecraft.tags.BlockTags;

public class ItemNet extends ItemToolIU {
    public ItemNet() {
        super(ModConfig.itemDouble("net_durability", 0.0D), -3, BlockTags.BEEHIVES);
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.net";
        }

        return this.nameItem;
    }
}
