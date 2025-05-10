package com.denfop.blocks.state;

public enum HarvestTool {
    None(null, -1),
    Pickaxe("pickaxe", 0),
    Shovel("shovel", 0),
    Axe("axe", 0),
    Wrench("wrench", 0);

    public final String toolClass;
    public final int level;

    HarvestTool(String toolClass, int level) {
        this.toolClass = toolClass;
        this.level = level;
    }
}
