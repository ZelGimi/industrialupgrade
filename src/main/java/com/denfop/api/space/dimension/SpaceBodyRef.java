package com.denfop.api.space.dimension;

import com.denfop.api.space.EnumRing;
import com.denfop.api.space.EnumType;
import net.minecraft.resources.ResourceLocation;

public record SpaceBodyRef(
        String name,
        Kind kind,
        String systemName,
        String starName,
        String parentName,
        ResourceLocation texture,
        int temperature,
        boolean pressure,
        boolean oxygen,
        boolean colonies,
        double size,
        double orbitDistance,
        double distanceFromStar,
        int rotationAngle,
        double orbitPeriod,
        EnumType type,
        EnumRing ring
) {

    public boolean isSatellite() {
        return this.kind == Kind.SATELLITE;
    }

    public boolean isAsteroid() {
        return this.kind == Kind.ASTEROID;
    }

    public enum Kind {
        PLANET,
        SATELLITE,
        ASTEROID
    }
}
