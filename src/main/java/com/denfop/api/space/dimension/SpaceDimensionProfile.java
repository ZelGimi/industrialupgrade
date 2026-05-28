package com.denfop.api.space.dimension;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public record SpaceDimensionProfile(
        SpaceBodyRef body,
        SpaceGenerationMode generationMode,
        ResourceLocation effectsLocation,
        ResourceKey<NoiseGeneratorSettings> baseNoiseSettings,
        float gravity,
        long dayLength,
        @Nullable Long fixedTime,
        boolean natural,
        boolean hasSkyLight,
        boolean hasCeiling,
        boolean ultraWarm,
        boolean bedWorks,
        boolean respawnAnchorWorks,
        boolean piglinSafe,
        boolean hasRaids,
        int minY,
        int height,
        int logicalHeight,
        int seaLevel,
        double coordinateScale,
        float ambientLight,
        float cloudHeight,
        TagKey<Block> infiniburn,
        boolean hasAtmosphere,
        float atmosphereDensity,
        Vec3 skyColor,
        Vec3 fogColor,
        int waterColor,
        int waterFogColor,
        float sunSize,
        float starBrightness,
        boolean drawStars,
        boolean drawSunrise,
        float fogDistanceFactor,
        SpaceTerrainPalette terrain,
        boolean generateCraters,
        boolean generateOres,
        boolean generateLakes,
        boolean generateGeysers,
        boolean generateVolcanoes,
        boolean generateCavities,
        boolean generateVerticalShafts,
        boolean generateLavaTubes,
        boolean generateAsteroidField,
        int craterRarity,
        int volcanoRarity,
        int cavityRarity,
        int shaftRarity,
        int lavaTubeRarity,
        @Nullable AmbientParticleSettings ambientParticle,
        @Nullable Music music
) {
    public static SpaceDimensionProfile none(final SpaceBodyRef body, final SpaceTerrainPalette terrain, final ResourceLocation effects) {
        return new SpaceDimensionProfile(
                body,
                SpaceGenerationMode.NONE,
                effects,
                NoiseGeneratorSettings.OVERWORLD,
                0.08F,
                24000L,
                6000L,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false,
                -64,
                384,
                256,
                -63,
                1.0D,
                0.0F,
                0.0F,
                BlockTags.INFINIBURN_OVERWORLD,
                false,
                0.0F,
                new Vec3(0.02D, 0.02D, 0.03D),
                new Vec3(0.02D, 0.02D, 0.03D),
                4159204,
                329011,
                0.8F,
                1.0F,
                true,
                false,
                0.12F,
                terrain,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                null,
                null
        );
    }

    public BlockState defaultBlock() {
        return this.terrain.defaultBlock();
    }

    public BlockState topBlock() {
        return this.terrain.topBlock();
    }

    public BlockState subsurfaceBlock() {
        return this.terrain.subsurfaceBlock();
    }

    public BlockState rimBlock() {
        return this.terrain.rimBlock();
    }

    public BlockState cobbleBlock() {
        return this.terrain.cobbleBlock();
    }

    public BlockState defaultFluid() {
        return this.terrain.defaultFluid();
    }

    public List<SpaceOreEntry> ores() {
        return this.terrain.ores();
    }

    public List<SpaceFluidPocket> fluids() {
        return this.terrain.fluids();
    }

    public boolean generatesTerrain() {
        return this.generationMode != SpaceGenerationMode.NONE && !this.generateAsteroidField;
    }

    public boolean isAsteroidField() {
        return this.generationMode == SpaceGenerationMode.ASTEROID_FIELD || this.generateAsteroidField;
    }

    public float temperatureForBiome() {
        return Math.max(-0.7F, Math.min(2.5F, this.body.temperature() / 100.0F + 0.8F));
    }

    public float downfall() {
        return this.hasAtmosphere ? Math.min(1.0F, this.atmosphereDensity * 0.7F) : 0.0F;
    }
}
