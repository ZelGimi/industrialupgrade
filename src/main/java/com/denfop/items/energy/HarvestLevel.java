package com.denfop.items.energy;

import net.minecraft.item.Item.ToolMaterial;

public enum HarvestLevel {
    Wood(0, ToolMaterial.WOOD),
    Stone(1, ToolMaterial.STONE),
    Iron(2, ToolMaterial.IRON),
    Diamond(3, ToolMaterial.DIAMOND),
    Iridium(100, ToolMaterial.DIAMOND);

    public final int level;
    public final ToolMaterial toolMaterial;

    HarvestLevel(int level, ToolMaterial toolMaterial) {
        this.level = level;
        this.toolMaterial = toolMaterial;
    }
}
