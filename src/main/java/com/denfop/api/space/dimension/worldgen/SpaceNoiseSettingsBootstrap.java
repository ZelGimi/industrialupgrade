package com.denfop.api.space.dimension.worldgen;

import com.denfop.IUCore;
import com.denfop.api.space.dimension.SpaceBodyCatalog;
import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionKeys;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public final class SpaceNoiseSettingsBootstrap {

    private SpaceNoiseSettingsBootstrap() {
    }

    public static void bootstrap(final BootstrapContext<NoiseGeneratorSettings> context) {
        for (var body : SpaceBodyCatalog.allBodies()) {
            final SpaceDimensionProfile profile = SpaceBodyProfiles.byBody(body);
            final NoiseGeneratorSettings base = NoiseGeneratorSettings.overworld(context, false, false);

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

            context.register(SpaceDimensionKeys.noiseSettingsKey(body.name()), settings);
        }
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