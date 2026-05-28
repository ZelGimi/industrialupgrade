package com.denfop.api.space.dimension.worldgen;

import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceBodyRef;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractSpaceExoticFeature<C extends FeatureConfiguration> extends Feature<C> {

    static Map<ResourceLocation, SpaceBodyRef> spaceBodyRefMap = new HashMap<>();

    protected AbstractSpaceExoticFeature(Codec<C> codec) {
        super(codec);
    }

    public static SpaceDimensionProfile resolveProfile(ResourceLocation dimensionId) {
        SpaceBodyRef bodyProfiles = spaceBodyRefMap.get(dimensionId);
        if (bodyProfiles != null) {
            return SpaceBodyProfiles.byBody(bodyProfiles);
        } else {
            try {
                for (SpaceBodyRef body : SpaceBodyCatalog.allBodies()) {
                    if (matchesBody(body, dimensionId)) {
                        spaceBodyRefMap.put(dimensionId, body);
                        return SpaceBodyProfiles.byBody(body);
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static boolean matchesBody(SpaceBodyRef body, ResourceLocation dimensionId) {
        final String path = dimensionId.getPath().toLowerCase(Locale.ROOT);
        final String bodyName = body.name().toLowerCase(Locale.ROOT);

        return bodyName.startsWith(path);
    }

    protected SpacePlanetTraits traits(FeaturePlaceContext<C> context) {
        return SpacePlanetTraits.from(context.level());
    }

    protected SpacePlanetTraits traits(WorldGenLevel context) {
        return SpacePlanetTraits.from(context);
    }

    protected SpacePlanetTraits traits(Level context) {
        return SpacePlanetTraits.from(context);
    }

    protected SpaceDimensionProfile profile(FeaturePlaceContext<C> context, SpacePlanetTraits traits) {
        return resolveProfile(traits.dimensionId());
    }

    protected SpaceMaterialSet materials(FeaturePlaceContext<C> context, SpacePlanetTraits traits) {
        return SpaceMaterialSet.resolve(
                context.level(),
                context.origin(),
                traits,
                profile(context, traits)
        );
    }

    protected BlockPos undergroundOrigin(FeaturePlaceContext<C> context, int ceilingOffset) {
        BlockPos origin = context.origin();
        int surface = context.level().getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG,
                origin.getX(),
                origin.getZ()
        );
        int maxY = Math.max(context.level().getMinBuildHeight() + 16, surface - ceilingOffset);
        int y = Mth.clamp(origin.getY(), context.level().getMinBuildHeight() + 16, maxY);
        return new BlockPos(origin.getX(), y, origin.getZ());
    }

    protected BlockPos surfaceOrigin(FeaturePlaceContext<C> context) {
        BlockPos origin = context.origin();
        int y = SpaceCarvingSupport.surfaceY(context.level(), origin.getX(), origin.getZ());
        return new BlockPos(origin.getX(), y, origin.getZ());
    }

    protected boolean insideBuildHeight(WorldGenLevel level, int y) {
        return y > level.getMinBuildHeight() + 1 && y < level.getMaxBuildHeight() - 1;
    }
}