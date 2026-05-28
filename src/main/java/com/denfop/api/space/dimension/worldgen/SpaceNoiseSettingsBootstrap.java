package com.denfop.api.space.dimension.worldgen;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SpaceNoiseSettingsBootstrap {

    private SpaceNoiseSettingsBootstrap() {
    }

    public static Map<ResourceLocation, NoiseGeneratorSettings> build(final RegistryAccess registryAccess) {
        final Map<ResourceLocation, NoiseGeneratorSettings> entries = new LinkedHashMap<>();
        for (var body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final NoiseGeneratorSettings base = baseOverworld(registryAccess);

            final SurfaceRules.RuleSource surfaceRules = createSurfaceRules(body.name(), profile);

            final NoiseGeneratorSettings settings;
            if (!profile.generatesTerrain()) {
                settings = new NoiseGeneratorSettings(
                        base.noiseSettings(),
                        Blocks.AIR.defaultBlockState(),
                        Blocks.AIR.defaultBlockState(),
                        base.noiseRouter(),
                        surfaceRules,
                        base.spawnTarget(),
                        -63,
                        false,
                        false,
                        false,
                        base.useLegacyRandomSource()
                );
            } else {
                settings = new NoiseGeneratorSettings(
                        base.noiseSettings(),
                        profile.defaultBlock(),
                        profile.defaultFluid(),
                        base.noiseRouter(),
                        surfaceRules,
                        base.spawnTarget(),
                        profile.seaLevel(),
                        false,
                        base.aquifersEnabled(),
                        false, // вимикає ванільні iron/copper ore veins
                        base.useLegacyRandomSource()
                );
            }

            register(entries, SpaceDimensionKeys.noiseSettingsKey(body.name()), settings);
        }

        return entries;
    }

    private static void register(
            final Map<ResourceLocation, NoiseGeneratorSettings> entries,
            final ResourceKey<NoiseGeneratorSettings> key,
            final NoiseGeneratorSettings value
    ) {
        entries.put(key.location(), value);
    }

    private static NoiseGeneratorSettings baseOverworld(final RegistryAccess registryAccess) {
        final ResourceKey<NoiseGeneratorSettings> overworldKey = ResourceKey.create(
                Registry.NOISE_GENERATOR_SETTINGS_REGISTRY,
                new ResourceLocation("minecraft", "overworld")
        );
        return registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY)
                .getOrCreateHolderOrThrow(overworldKey)
                .value();
    }

    private static SurfaceRules.RuleSource createSurfaceRules(
            final String bodyName,
            final SpaceDimensionProfile profile
    ) {
        final SurfaceRules.RuleSource bedrock = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());

        final SurfaceRules.RuleSource floorBedrock = SurfaceRules.ifTrue(
                SurfaceRules.verticalGradient(
                        IUCore.MODID + ":space_bedrock_floor_" + bodyName,
                        VerticalAnchor.aboveBottom(0),
                        VerticalAnchor.aboveBottom(5)
                ),
                bedrock
        );

        final SurfaceRules.RuleSource terrainOrAir = profile.generatesTerrain()
                ? SpaceSurfaceRuleFactory.create(profile)
                : SurfaceRules.state(Blocks.AIR.defaultBlockState());

        if (profile.hasCeiling()) {
            final SurfaceRules.RuleSource roofBedrock = SurfaceRules.ifTrue(
                    SurfaceRules.verticalGradient(
                            IUCore.MODID + ":space_bedrock_roof_" + bodyName,
                            VerticalAnchor.belowTop(5),
                            VerticalAnchor.belowTop(0)
                    ),
                    bedrock
            );

            return SurfaceRules.sequence(
                    roofBedrock,
                    floorBedrock,
                    terrainOrAir
            );
        }

        return SurfaceRules.sequence(
                floorBedrock,
                terrainOrAir
        );
    }
}