package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpaceCraterFeatureConfig(
        BlockState primary,
        BlockState rim,
        BlockState ejecta,
        SpawnSettings spawn,
        CraterBands bands,
        boolean centralPeaks,
        boolean ejectaBlanket
) implements FeatureConfiguration {

    public static final Codec<SpaceCraterFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("primary").forGetter(SpaceCraterFeatureConfig::primary),
            BlockState.CODEC.fieldOf("rim").forGetter(SpaceCraterFeatureConfig::rim),
            BlockState.CODEC.fieldOf("ejecta").forGetter(SpaceCraterFeatureConfig::ejecta),
            SpawnSettings.CODEC.fieldOf("spawn").forGetter(SpaceCraterFeatureConfig::spawn),
            CraterBands.CODEC.fieldOf("bands").forGetter(SpaceCraterFeatureConfig::bands),
            Codec.BOOL.fieldOf("central_peaks").forGetter(SpaceCraterFeatureConfig::centralPeaks),
            Codec.BOOL.fieldOf("ejecta_blanket").forGetter(SpaceCraterFeatureConfig::ejectaBlanket)
    ).apply(instance, SpaceCraterFeatureConfig::new));

    public static SpaceCraterFeatureConfig defaultConfig(
            final BlockState primary,
            final BlockState rim,
            final BlockState ejecta
    ) {
        return new SpaceCraterFeatureConfig(
                primary,
                rim,
                ejecta,
                new SpawnSettings(2, 0.22F),
                new CraterBands(
                        new CraterBand(4, 7, 0.58F),
                        new CraterBand(8, 14, 0.25F),
                        new CraterBand(15, 26, 0.12F),
                        new CraterBand(27, 44, 0.05F)
                ),
                true,
                true
        );
    }

    public static SpaceCraterFeatureConfig denseConfig(
            final BlockState primary,
            final BlockState rim,
            final BlockState ejecta
    ) {
        return new SpaceCraterFeatureConfig(
                primary,
                rim,
                ejecta,
                new SpawnSettings(3, 0.30F),
                new CraterBands(
                        new CraterBand(4, 8, 0.56F),
                        new CraterBand(9, 16, 0.25F),
                        new CraterBand(17, 30, 0.13F),
                        new CraterBand(31, 52, 0.06F)
                ),
                true,
                true
        );
    }

    public int attemptsPerCandidateChunk() {
        return this.spawn.attemptsPerCandidateChunk();
    }

    public float spawnChance() {
        return this.spawn.spawnChance();
    }

    public int smallMinRadius() {
        return this.bands.small().minRadius();
    }

    public int smallMaxRadius() {
        return this.bands.small().maxRadius();
    }

    public int mediumMinRadius() {
        return this.bands.medium().minRadius();
    }

    public int mediumMaxRadius() {
        return this.bands.medium().maxRadius();
    }

    public int largeMinRadius() {
        return this.bands.large().minRadius();
    }

    public int largeMaxRadius() {
        return this.bands.large().maxRadius();
    }

    public int giantMinRadius() {
        return this.bands.giant().minRadius();
    }

    public int giantMaxRadius() {
        return this.bands.giant().maxRadius();
    }

    public float smallWeight() {
        return this.bands.small().weight();
    }

    public float mediumWeight() {
        return this.bands.medium().weight();
    }

    public float largeWeight() {
        return this.bands.large().weight();
    }

    public float giantWeight() {
        return this.bands.giant().weight();
    }

    public record SpawnSettings(
            int attemptsPerCandidateChunk,
            float spawnChance
    ) {
        public static final Codec<SpawnSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(1, 4).fieldOf("attempts_per_candidate_chunk").forGetter(SpawnSettings::attemptsPerCandidateChunk),
                Codec.floatRange(0.0F, 1.0F).fieldOf("spawn_chance").forGetter(SpawnSettings::spawnChance)
        ).apply(instance, SpawnSettings::new));
    }

    public record CraterBand(
            int minRadius,
            int maxRadius,
            float weight
    ) {
        public static final Codec<CraterBand> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(2, 128).fieldOf("min_radius").forGetter(CraterBand::minRadius),
                Codec.intRange(2, 128).fieldOf("max_radius").forGetter(CraterBand::maxRadius),
                Codec.floatRange(0.0F, 10.0F).fieldOf("weight").forGetter(CraterBand::weight)
        ).apply(instance, CraterBand::new));
    }

    public record CraterBands(
            CraterBand small,
            CraterBand medium,
            CraterBand large,
            CraterBand giant
    ) {
        public static final Codec<CraterBands> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CraterBand.CODEC.fieldOf("small").forGetter(CraterBands::small),
                CraterBand.CODEC.fieldOf("medium").forGetter(CraterBands::medium),
                CraterBand.CODEC.fieldOf("large").forGetter(CraterBands::large),
                CraterBand.CODEC.fieldOf("giant").forGetter(CraterBands::giant)
        ).apply(instance, CraterBands::new));
    }
}