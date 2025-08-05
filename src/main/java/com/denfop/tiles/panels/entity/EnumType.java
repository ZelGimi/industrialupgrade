package com.denfop.tiles.panels.entity;


import com.denfop.Constants;
import net.minecraft.resources.ResourceLocation;

public enum EnumType {
    DEFAULT(0, 0, 1, 1, 0.65, 0, "Advanced"),
    AIR(0, 0, 1.5, 1.5, 0.65, 1, "aer"),
    EARTH(0, 0, 1.5, 1.5, 0.65, 2, "earth"),
    NETHER(1, 0, 1, 1, 0.65, 3, "nether"),
    END(0, 1, 1, 1, 0.65, 4, "end"),
    NIGHT(0, 0, 1, 1.5, 0.65, 5, "night"),
    DAY(0, 0, 1.5, 1, 0.65, 6, "sun"),
    RAIN(0, 0, 1, 1, 1.5, 7, "rain");
    public final double coefficient_day;
    public final double coefficient_night;
    public final double coefficient_rain;
    public final int meta;
    public final ResourceLocation texture;
    public final double coefficient_nether;
    public final double coefficient_end;
    private final String nameType;

    EnumType(
            double coefficient_nether,
            double coefficient_end,
            double coefficient_day,
            double coefficient_night,
            double coefficient_rain,
            int meta,
            String name
    ) {
        this.coefficient_day = coefficient_day;
        this.coefficient_night = coefficient_night;
        this.coefficient_rain = coefficient_rain;
        this.meta = meta;
        this.coefficient_nether = coefficient_nether;
        this.coefficient_end = coefficient_end;
        this.texture = ResourceLocation.tryBuild(
                Constants.MOD_ID,
                ("textures/gui/GUI" + name + "SolarPanel.png").toLowerCase()
        );
        this.nameType = name;
    }

    public static EnumType getFromID(final int ID) {
        final EnumType[] values = values();
        return values[ID % values.length];
    }

    public String getNameType() {
        return nameType;
    }
}
