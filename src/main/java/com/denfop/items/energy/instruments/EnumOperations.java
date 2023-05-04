package com.denfop.items.energy.instruments;

import ic2.core.init.Localization;
import net.minecraft.util.text.TextFormatting;

public enum EnumOperations {
    DEFAULT("message.ultDDrill.mode.normal", "message.description.normal", TextFormatting.GREEN, 1, 1, 1),
    BIGHOLES("message.ultDDrill.mode.bigHoles", "message.description.bigHoles", TextFormatting.AQUA, 3, 3, 1),
    MEGAHOLES("message.ultDDrill.mode.bigHoles1", "message.description.bigHoles1", TextFormatting.LIGHT_PURPLE, 5, 5, 1),
    ULTRAHOLES("message.ultDDrill.mode.bigHoles2", "message.description.bigHoles2", TextFormatting.DARK_PURPLE, 7, 7, 1),

    ORE("message.ultDDrill.mode.pickaxe", "message.description.pickaxe", TextFormatting.DARK_AQUA, 1, 1, 1),
    TREE("message.ultDDrill.mode.treemode", "message.description.treemode", TextFormatting.DARK_GREEN, 1, 1, 1),
    TUNNEL("message.ultDDrill.mode.tunel", "message.description.tunel", TextFormatting.BLUE, 1, 1, 8),
    SHEARS("message.ultDDrill.mode.shears", "message.description.shears", TextFormatting.DARK_GREEN, 1, 1, 1);

    private final String name_mode;
    private final String description;
    private final TextFormatting textFormatting;
    private final int area_x;
    private final int area_y;
    private final int area_z;

    EnumOperations(String localization, String description, TextFormatting textFormatting, int area_x, int area_y, int area_z) {
        this.name_mode = Localization.translate(localization);
        this.description = Localization.translate(description);
        this.textFormatting = textFormatting;
        this.area_x = area_x;
        this.area_y = area_y;
        this.area_z = area_z;
    }

    public int getArea_x() {
        return area_x;
    }

    public int getArea_y() {
        return area_y;
    }

    public int getArea_z() {
        return area_z;
    }

    public TextFormatting getTextFormatting() {
        return textFormatting;
    }

    public String getDescription() {
        return description;
    }

    public String getName_mode() {
        return name_mode;
    }

}
