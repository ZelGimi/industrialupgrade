package com.denfop.api.space.dimension.worldgen.feature.asteroid;

import java.util.Locale;

public enum AsteroidMaterialType {
    ROCKY,
    METALLIC,
    MIXED,
    POROUS,
    ORE_RICH,
    HOLLOW,
    ICY,
    SPECIAL;

    public static AsteroidMaterialType byName(final String name) {
        if (name == null || name.isBlank()) {
            return ROCKY;
        }
        final String normalized = name.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        for (AsteroidMaterialType value : values()) {
            if (value.name().equals(normalized)) {
                return value;
            }
        }
        return ROCKY;
    }

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
