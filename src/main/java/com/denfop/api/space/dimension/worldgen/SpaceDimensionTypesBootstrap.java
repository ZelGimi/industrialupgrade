package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalLong;

public final class SpaceDimensionTypesBootstrap {

    private SpaceDimensionTypesBootstrap() {
    }

    public static Map<ResourceLocation, DimensionType> build() {
        final Map<ResourceLocation, DimensionType> entries = new LinkedHashMap<>();
        for (var body : SpaceBodyCatalog.allBodies()) {
            SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            register(entries, SpaceDimensionKeys.dimensionTypeKey(body.name()), new DimensionType(
                    profile.fixedTime() == null ? OptionalLong.empty() : OptionalLong.of(profile.fixedTime()),
                    profile.hasSkyLight(),
                    profile.hasCeiling(),
                    false,
                    profile.natural(),
                    profile.coordinateScale(),
                    profile.bedWorks(),
                    profile.respawnAnchorWorks(),
                    profile.minY(),
                    profile.height(),
                    profile.logicalHeight(),
                    profile.infiniburn(),
                    profile.effectsLocation(),
                    profile.ambientLight(),
                    new DimensionType.MonsterSettings(profile.piglinSafe(), profile.hasRaids(), ConstantInt.of(profile.hasSkyLight() ? 7 : 0), profile.hasSkyLight() ? 0 : 15)
            ));
        }

        return entries;
    }

    private static void register(
            final Map<ResourceLocation, DimensionType> entries,
            final ResourceKey<DimensionType> key,
            final DimensionType value
    ) {
        entries.put(key.location(), value);
    }
}

