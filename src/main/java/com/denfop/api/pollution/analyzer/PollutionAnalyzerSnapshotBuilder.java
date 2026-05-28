package com.denfop.api.pollution.analyzer;

import com.denfop.IUItem;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.BlockEntityPurifierSoil;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityAirCollector;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;
import java.util.stream.Collectors;

public final class PollutionAnalyzerSnapshotBuilder {

    private static final int DISPLAY_RADIUS = 1;
    private static final int SIMULATION_RADIUS = 8;

    private static final int TERRAIN_GRID = 16;
    private static final int MAX_UPPER_BLOCKS = 16;
    private static final int GRAPH_TICKS = 240;

    private static final int MAX_MECHANISM_LEVEL = 10;

    private PollutionAnalyzerSnapshotBuilder() {
    }

    public static PollutionAnalyzerSnapshot build(ServerLevel level, ChunkPos center) {
        PollutionManager manager = PollutionManager.pollutionManager;

        if (manager == null) {
            return empty(level, center);
        }

        List<PollutionAnalyzerSourceSnapshot> visibleSources = collectSources(level, center, DISPLAY_RADIUS);
        Map<ChunkPos, Double> airSourcesByChunk = buildAirSourceMap(visibleSources);
        Map<ChunkPos, Double> soilSourcesByChunk = buildSoilSourceMap(visibleSources);
        Map<ChunkPos, Double> airCleaningByChunk = buildAirCleaningMap(visibleSources);
        Map<ChunkPos, Double> soilCleaningByChunk = buildSoilCleaningMap(visibleSources);

        Map<ChunkPos, ChunkLevel> nearbyAir = collectNearby(manager.getPollutionAirMap(), center, SIMULATION_RADIUS);
        Map<ChunkPos, ChunkLevel> nearbySoil = collectNearby(manager.getPollutionSoilMap(), center, SIMULATION_RADIUS);

        Set<Long> visibleKeys = new LinkedHashSet<>();
        for (int dz = -DISPLAY_RADIUS; dz <= DISPLAY_RADIUS; dz++) {
            for (int dx = -DISPLAY_RADIUS; dx <= DISPLAY_RADIUS; dx++) {
                visibleKeys.add(ChunkPos.asLong(center.x + dx, center.z + dz));
            }
        }

        PollutionAnalyzerForecastEngine.Result forecast = PollutionAnalyzerForecastEngine.simulate(
                visibleKeys,
                nearbyAir,
                nearbySoil,
                airSourcesByChunk,
                soilSourcesByChunk,
                airCleaningByChunk,
                soilCleaningByChunk,
                level.getGameTime()
        );

        PollutionAnalyzerThresholds airThresholds = PollutionAnalyzerMath.airThresholds();
        PollutionAnalyzerThresholds soilThresholds = PollutionAnalyzerMath.soilThresholds();

        List<PollutionAnalyzerChunkSnapshot> chunkSnapshots = new ArrayList<>();
        for (long key : visibleKeys) {
            ChunkPos pos = new ChunkPos(key);
            ChunkLevel air = nearbyAir.get(pos);
            ChunkLevel soil = nearbySoil.get(pos);

            int nw = surfaceHeight(level, pos, 1, 1);
            int ne = surfaceHeight(level, pos, 14, 1);
            int sw = surfaceHeight(level, pos, 1, 14);
            int se = surfaceHeight(level, pos, 14, 14);
            int centerY = surfaceHeight(level, pos, 8, 8);

            double airSeverity = PollutionAnalyzerMath.severity(air);
            double soilSeverity = PollutionAnalyzerMath.severity(soil);

            PollutionAnalyzerForecastEngine.ChunkForecast airForecast = forecast.air(key);
            PollutionAnalyzerForecastEngine.ChunkForecast soilForecast = forecast.soil(key);

            float riskScore = Math.max(
                    PollutionAnalyzerMath.normalizedRisk(airSeverity, airThresholds),
                    PollutionAnalyzerMath.normalizedRisk(soilSeverity, soilThresholds)
            );

            List<PollutionAnalyzerTerrainColumn> terrainColumns = buildTerrainColumns(level, pos);

            double[] airTotalTickSeries = expandSecondSeriesToTicks(airSeverity, airForecast.getSeriesSeconds(), GRAPH_TICKS);
            double[] soilTotalTickSeries = expandSecondSeriesToTicks(soilSeverity, soilForecast.getSeriesSeconds(), GRAPH_TICKS);

            double[] airRateTickSeries = buildRateSeries(airTotalTickSeries);
            double[] soilRateTickSeries = buildRateSeries(soilTotalTickSeries);

            double airCleaning = airCleaningByChunk.getOrDefault(pos, 0.0D);
            double soilCleaning = soilCleaningByChunk.getOrDefault(pos, 0.0D);

            chunkSnapshots.add(new PollutionAnalyzerChunkSnapshot(
                    pos.x,
                    pos.z,
                    nw, ne, sw, se, centerY,
                    airSeverity,
                    air != null ? air.getPollution() : 0.0D,
                    air != null ? air.getLevelPollution().ordinal() : 0,
                    airForecast.getRatePerSecond(),
                    airForecast.getTimeToRiseSeconds(),
                    airForecast.getTimeToFallSeconds(),
                    airForecast.getSeverityAtFiveMinutes(),
                    airCleaning,
                    soilSeverity,
                    soil != null ? soil.getPollution() : 0.0D,
                    soil != null ? soil.getLevelPollution().ordinal() : 0,
                    soilForecast.getRatePerSecond(),
                    soilForecast.getTimeToRiseSeconds(),
                    soilForecast.getTimeToFallSeconds(),
                    soilForecast.getSeverityAtFiveMinutes(),
                    soilCleaning,
                    riskScore,
                    terrainColumns,
                    airRateTickSeries,
                    soilRateTickSeries,
                    airTotalTickSeries,
                    soilTotalTickSeries
            ));
        }

        chunkSnapshots.sort(Comparator
                .comparingInt(PollutionAnalyzerChunkSnapshot::getChunkZ)
                .thenComparingInt(PollutionAnalyzerChunkSnapshot::getChunkX));

        PollutionAnalyzerCleanerOption airCleaner = PollutionAnalyzerCleanerResolver.resolveAirCleaner(visibleSources);
        PollutionAnalyzerCleanerOption soilCleaner = PollutionAnalyzerCleanerResolver.resolveSoilCleaner(visibleSources);

        List<PollutionAnalyzerRecommendation> recommendations = buildRecommendations(
                center,
                chunkSnapshots,
                visibleSources,
                airCleaner,
                soilCleaner
        );

        return new PollutionAnalyzerSnapshot(
                true,
                level.dimension().location().toString(),
                level.getGameTime(),
                center.x,
                center.z,
                forecast.getWindSide(),
                forecast.getWindType(),
                airThresholds,
                soilThresholds,
                airCleaner,
                soilCleaner,
                chunkSnapshots,
                visibleSources,
                recommendations
        );
    }

    public static PollutionAnalyzerSnapshot build(ServerPlayer player) {
        if (player.level().dimension() != Level.OVERWORLD) {
            return new PollutionAnalyzerSnapshot(
                    false,
                    player.level().dimension().location().toString(),
                    player.level().getGameTime(),
                    new ChunkPos(player.blockPosition()).x,
                    new ChunkPos(player.blockPosition()).z,
                    "N",
                    "CALM",
                    PollutionAnalyzerMath.airThresholds(),
                    PollutionAnalyzerMath.soilThresholds(),
                    PollutionAnalyzerCleanerResolver.resolveAirCleaner(Collections.emptyList()),
                    PollutionAnalyzerCleanerResolver.resolveSoilCleaner(Collections.emptyList()),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }

        return build((ServerLevel) player.level(), new ChunkPos(player.blockPosition()));
    }

    private static PollutionAnalyzerSnapshot empty(ServerLevel level, ChunkPos center) {
        return new PollutionAnalyzerSnapshot(
                true,
                level.dimension().location().toString(),
                level.getGameTime(),
                center.x,
                center.z,
                "N",
                "CALM",
                PollutionAnalyzerMath.airThresholds(),
                PollutionAnalyzerMath.soilThresholds(),
                PollutionAnalyzerCleanerResolver.resolveAirCleaner(Collections.emptyList()),
                PollutionAnalyzerCleanerResolver.resolveSoilCleaner(Collections.emptyList()),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private static int surfaceHeight(ServerLevel level, ChunkPos chunkPos, int localX, int localZ) {
        int worldX = (chunkPos.x << 4) + localX;
        int worldZ = (chunkPos.z << 4) + localZ;
        return level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ);
    }

    private static List<PollutionAnalyzerTerrainColumn> buildTerrainColumns(ServerLevel level, ChunkPos chunkPos) {
        List<PollutionAnalyzerTerrainColumn> result = new ArrayList<>(TERRAIN_GRID * TERRAIN_GRID);

        for (int localZ = 0; localZ < TERRAIN_GRID; localZ++) {
            for (int localX = 0; localX < TERRAIN_GRID; localX++) {
                int worldX = (chunkPos.x << 4) + localX;
                int worldZ = (chunkPos.z << 4) + localZ;

                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;
                int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, worldX, worldZ) - 1;

                if (groundY < level.getMinBuildHeight()) {
                    groundY = surfaceY;
                }

                BlockPos groundPos = new BlockPos(worldX, Math.max(level.getMinBuildHeight(), groundY), worldZ);
                Block groundBlock = level.getBlockState(groundPos).getBlock();

                List<String> upperBlocks = new ArrayList<>();
                int maxY = Math.min(surfaceY, groundY + MAX_UPPER_BLOCKS);
                for (int y = groundY + 1; y <= maxY; y++) {
                    Block block = level.getBlockState(new BlockPos(worldX, y, worldZ)).getBlock();
                    if (!block.defaultBlockState().isAir()) {
                        upperBlocks.add(BuiltInRegistries.BLOCK.getKey(block).toString());
                    }
                }

                result.add(new PollutionAnalyzerTerrainColumn(
                        localX,
                        localZ,
                        groundY,
                        surfaceY,
                        BuiltInRegistries.BLOCK.getKey(groundBlock).toString(),
                        upperBlocks
                ));
            }
        }

        return result;
    }

    private static Map<ChunkPos, ChunkLevel> collectNearby(Map<ChunkPos, ChunkLevel> source, ChunkPos center, int radius) {
        Map<ChunkPos, ChunkLevel> result = new HashMap<>();
        for (Map.Entry<ChunkPos, ChunkLevel> entry : source.entrySet()) {
            ChunkPos pos = entry.getKey();
            if (Math.abs(pos.x - center.x) <= radius && Math.abs(pos.z - center.z) <= radius) {
                ChunkLevel src = entry.getValue();
                ChunkLevel copy = new ChunkLevel(new ChunkPos(src.getPos().x, src.getPos().z), src.getLevelPollution(), src.getPollution());
                copy.setDefaultPos(new ChunkPos(src.getDefaultPos().x, src.getDefaultPos().z));
                result.put(copy.getPos(), copy);
            }
        }
        return result;
    }

    private static Map<ChunkPos, Double> buildAirSourceMap(List<PollutionAnalyzerSourceSnapshot> sources) {
        Map<ChunkPos, Double> result = new HashMap<>();
        for (PollutionAnalyzerSourceSnapshot source : sources) {
            if (source.getAirCurrentContribution() <= 0.0D) {
                continue;
            }
            ChunkPos pos = new ChunkPos(source.getChunkX(), source.getChunkZ());
            result.merge(pos, source.getAirCurrentContribution(), Double::sum);
        }
        return result;
    }

    private static Map<ChunkPos, Double> buildSoilSourceMap(List<PollutionAnalyzerSourceSnapshot> sources) {
        Map<ChunkPos, Double> result = new HashMap<>();
        for (PollutionAnalyzerSourceSnapshot source : sources) {
            if (source.getSoilCurrentContribution() <= 0.0D) {
                continue;
            }
            ChunkPos pos = new ChunkPos(source.getChunkX(), source.getChunkZ());
            result.merge(pos, source.getSoilCurrentContribution(), Double::sum);
        }
        return result;
    }

    private static Map<ChunkPos, Double> buildAirCleaningMap(List<PollutionAnalyzerSourceSnapshot> sources) {
        Map<ChunkPos, Double> result = new HashMap<>();
        for (PollutionAnalyzerSourceSnapshot source : sources) {
            if (source.getAirCleaningPerSecond() <= 0.0D) {
                continue;
            }
            ChunkPos pos = new ChunkPos(source.getChunkX(), source.getChunkZ());
            result.merge(pos, source.getAirCleaningPerSecond(), Double::sum);
        }
        return result;
    }

    private static Map<ChunkPos, Double> buildSoilCleaningMap(List<PollutionAnalyzerSourceSnapshot> sources) {
        Map<ChunkPos, Double> result = new HashMap<>();
        for (PollutionAnalyzerSourceSnapshot source : sources) {
            if (source.getSoilCleaningPerSecond() <= 0.0D) {
                continue;
            }
            ChunkPos pos = new ChunkPos(source.getChunkX(), source.getChunkZ());
            result.merge(pos, source.getSoilCleaningPerSecond(), Double::sum);
        }
        return result;
    }

    private static List<PollutionAnalyzerSourceSnapshot> collectSources(ServerLevel level, ChunkPos center, int radius) {
        List<PollutionAnalyzerSourceSnapshot> result = new ArrayList<>();

        for (int dz = -radius; dz <= radius; dz++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int chunkX = center.x + dx;
                int chunkZ = center.z + dz;

                if (!level.hasChunk(chunkX, chunkZ)) {
                    continue;
                }

                LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity == null || blockEntity.isRemoved()) {
                        continue;
                    }

                    double airCurrent = 0.0D;
                    double airBase = 0.0D;
                    double airPercent = 1.0D;

                    double soilCurrent = 0.0D;
                    double soilBase = 0.0D;
                    double soilPercent = 1.0D;

                    boolean cleaner = false;
                    String cleanerMedium = "";
                    int mechanismLevel = 0;
                    int maxLevel = MAX_MECHANISM_LEVEL;
                    double airCleaning = 0.0D;
                    double soilCleaning = 0.0D;

                    if (blockEntity instanceof BlockEntityBase base) {

                        AirPollutionComponent air = base.getComp(AirPollutionComponent.class);
                        SoilPollutionComponent soil = base.getComp(SoilPollutionComponent.class);

                        airCurrent = air != null && base.getActive() ? air.getCurrentContribution() : 0.0D;
                        soilCurrent = soil != null && base.getActive() ? soil.getCurrentContribution() : 0.0D;

                        airBase = air != null && base.getActive() ? air.getDefault_pollution() : 0.0D;
                        soilBase = soil != null && base.getActive() ? soil.getDefault_pollution() : 0.0D;

                        airPercent = air != null && base.getActive() ? air.getPercent() : 1.0D;
                        soilPercent = soil != null && base.getActive() ? soil.getPercent() : 1.0D;
                    }

                    if (blockEntity instanceof BlockEntityAirCollector collector) {
                        cleaner = true;
                        cleanerMedium = "air";
                        mechanismLevel = collector.getLevelMechanism();
                        airCleaning += airCurrent > 0 ? getAirCollectorCleaningPerSecond(mechanismLevel) : 0;
                    }

                    if (blockEntity instanceof BlockEntityPurifierSoil purifier) {
                        cleaner = true;
                        cleanerMedium = "soil";
                        mechanismLevel = purifier.getLevelMechanism();
                        soilCleaning += soilPercent > 0 ? getSoilPurifierCleaningPerSecond(mechanismLevel) : 0;


                        airCurrent += soilPercent > 0 ? getSoilPurifierAirSideEffectPerSecond(mechanismLevel) : 0;
                    }

                    if (airCurrent <= 0.0D
                            && soilCurrent <= 0.0D
                            && airCleaning <= 0.0D
                            && soilCleaning <= 0.0D) {
                        continue;
                    }

                    BlockPos pos = blockEntity.getBlockPos();
                    String registryName = BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock()).toString();
                    ItemStack renderStack = new ItemStack(blockEntity.getBlockState().getBlock().asItem());

                    result.add(new PollutionAnalyzerSourceSnapshot(
                            registryName + "@" + pos.getX() + "," + pos.getY() + "," + pos.getZ(),
                            blockEntity.getBlockState().getBlock().getName().getString(),
                            registryName,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            new ChunkPos(pos).x,
                            new ChunkPos(pos).z,
                            renderStack,
                            true,
                            airCurrent,
                            airBase,
                            airPercent,
                            soilCurrent,
                            soilBase,
                            soilPercent,
                            cleaner,
                            cleanerMedium,
                            mechanismLevel,
                            maxLevel,
                            airCleaning,
                            soilCleaning
                    ));
                }
            }
        }

        result.sort(Comparator.comparingDouble(
                source -> ((PollutionAnalyzerSourceSnapshot) source).getTotalCurrentContribution() + ((PollutionAnalyzerSourceSnapshot) source).getTotalCleaningPerSecond()
        ).reversed());

        return result;
    }

    private static List<PollutionAnalyzerRecommendation> buildRecommendations(
            ChunkPos center,
            List<PollutionAnalyzerChunkSnapshot> chunks,
            List<PollutionAnalyzerSourceSnapshot> sources,
            PollutionAnalyzerCleanerOption airCleaner,
            PollutionAnalyzerCleanerOption soilCleaner
    ) {
        List<PollutionAnalyzerRecommendation> recommendations = new ArrayList<>();

        PollutionAnalyzerChunkSnapshot worstAirChunk = chunks.stream()
                .max(Comparator.comparingDouble(PollutionAnalyzerChunkSnapshot::getAirSeverity))
                .orElse(null);

        PollutionAnalyzerChunkSnapshot worstSoilChunk = chunks.stream()
                .max(Comparator.comparingDouble(PollutionAnalyzerChunkSnapshot::getSoilSeverity))
                .orElse(null);

        PollutionAnalyzerSourceSnapshot dominantAir = sources.stream()
                .filter(source -> source.getAirCurrentContribution() > 0.0D)
                .max(Comparator.comparingDouble(PollutionAnalyzerSourceSnapshot::getAirCurrentContribution))
                .orElse(null);

        PollutionAnalyzerSourceSnapshot dominantSoil = sources.stream()
                .filter(source -> source.getSoilCurrentContribution() > 0.0D)
                .max(Comparator.comparingDouble(PollutionAnalyzerSourceSnapshot::getSoilCurrentContribution))
                .orElse(null);

        boolean hasAirCleanerInstalled = sources.stream()
                .anyMatch(source -> source.isCleaner() && "air".equals(source.getCleanerMedium()));

        boolean hasSoilCleanerInstalled = sources.stream()
                .anyMatch(source -> source.isCleaner() && "soil".equals(source.getCleanerMedium()));

        if (dominantAir != null) {
            recommendations.add(new PollutionAnalyzerRecommendation(
                    3,
                    "iu.pollution_analyzer.rec.air_source.title",
                    "iu.pollution_analyzer.rec.air_source.body",
                    List.of(
                            dominantAir.getName(),
                            String.valueOf(dominantAir.getBlockX()),
                            String.valueOf(dominantAir.getBlockY()),
                            String.valueOf(dominantAir.getBlockZ()),
                            formatRate("air", dominantAir.getAirCurrentContribution())
                    )
            ));
        }

        if (dominantSoil != null) {
            recommendations.add(new PollutionAnalyzerRecommendation(
                    3,
                    "iu.pollution_analyzer.rec.soil_source.title",
                    "iu.pollution_analyzer.rec.soil_source.body",
                    List.of(
                            dominantSoil.getName(),
                            String.valueOf(dominantSoil.getBlockX()),
                            String.valueOf(dominantSoil.getBlockY()),
                            String.valueOf(dominantSoil.getBlockZ()),
                            formatRate("soil", dominantSoil.getSoilCurrentContribution())
                    )
            ));
        }

        if (shouldRecommendAirCleaner(worstAirChunk, dominantAir, hasAirCleanerInstalled)) {
            PollutionAnalyzerChunkSnapshot targetChunk = worstAirChunk != null
                    ? worstAirChunk
                    : chunks.stream().findFirst().orElse(null);

            if (targetChunk != null) {
                double needCompensate = requiredAirCompensation(targetChunk, dominantAir);

                String bodyKey = airCleaner.getRecommendedSingleMachineLevel() >= 0
                        ? "iu.pollution_analyzer.rec.air_cleaner_plan.body_dual_single"
                        : "iu.pollution_analyzer.rec.air_cleaner_plan.body_dual_only";

                List<String> args = new ArrayList<>();
                args.add(Localization.translate(IUItem.basemachine2.getItemStack(BlockBaseMachine3Entity.aircollector).getDescriptionId()));
                args.add(String.valueOf(targetChunk.getChunkX()));
                args.add(String.valueOf(targetChunk.getChunkZ()));
                args.add(formatRate("air", needCompensate));
                args.add(formatRate("air", airCleaner.getBaseReductionPerSecond()));
                args.add(formatRate("air", airCleaner.getLevelBonusReductionPerSecond()));
                args.add(String.valueOf(airCleaner.getRecommendedBaseMachineCount()));
                args.add(String.valueOf(airCleaner.getMaxLevel()));

                if (airCleaner.getRecommendedSingleMachineLevel() >= 0) {
                    args.add(String.valueOf(airCleaner.getRecommendedSingleMachineLevel()));
                    args.add(String.valueOf(airCleaner.getMaxLevel()));
                }

                recommendations.add(new PollutionAnalyzerRecommendation(
                        hasAirCleanerInstalled ? 2 : 3,
                        "iu.pollution_analyzer.rec.air_cleaner_plan.title",
                        bodyKey,
                        args
                ));
            }
        }

        if (shouldRecommendSoilCleaner(worstSoilChunk, dominantSoil, hasSoilCleanerInstalled)) {
            PollutionAnalyzerChunkSnapshot targetChunk = worstSoilChunk != null
                    ? worstSoilChunk
                    : chunks.stream().findFirst().orElse(null);

            if (targetChunk != null) {
                double needCompensate = requiredSoilCompensation(targetChunk, dominantSoil);

                String bodyKey = soilCleaner.getRecommendedSingleMachineLevel() >= 0
                        ? "iu.pollution_analyzer.rec.soil_cleaner_plan.body_dual_single"
                        : "iu.pollution_analyzer.rec.soil_cleaner_plan.body_dual_only";

                List<String> args = new ArrayList<>();
                args.add(Localization.translate(IUItem.basemachine2.getItemStack(BlockBaseMachine3Entity.purifier_soil).getDescriptionId()));
                args.add(String.valueOf(targetChunk.getChunkX()));
                args.add(String.valueOf(targetChunk.getChunkZ()));
                args.add(formatRate("soil", needCompensate));
                args.add(formatRate("soil", soilCleaner.getBaseReductionPerSecond()));
                args.add(formatRate("soil", soilCleaner.getLevelBonusReductionPerSecond()));
                args.add(String.valueOf(soilCleaner.getRecommendedBaseMachineCount()));
                args.add(String.valueOf(soilCleaner.getMaxLevel()));

                if (soilCleaner.getRecommendedSingleMachineLevel() >= 0) {
                    args.add(String.valueOf(soilCleaner.getRecommendedSingleMachineLevel()));
                    args.add(String.valueOf(soilCleaner.getMaxLevel()));
                }

                recommendations.add(new PollutionAnalyzerRecommendation(
                        hasSoilCleanerInstalled ? 2 : 3,
                        "iu.pollution_analyzer.rec.soil_cleaner_plan.title",
                        bodyKey,
                        args
                ));
            }
        }

        if (dominantAir != null) {
            recommendations.add(new PollutionAnalyzerRecommendation(
                    1,
                    "iu.pollution_analyzer.rec.air_module.title",
                    "iu.pollution_analyzer.rec.air_module.body",
                    List.of(
                            dominantAir.getName(),
                            String.valueOf(dominantAir.getBlockX()),
                            String.valueOf(dominantAir.getBlockY()),
                            String.valueOf(dominantAir.getBlockZ()),
                            formatRate("air", dominantAir.getAirCurrentContribution())
                    )
            ));
        }

        if (dominantSoil != null) {
            recommendations.add(new PollutionAnalyzerRecommendation(
                    1,
                    "iu.pollution_analyzer.rec.soil_module.title",
                    "iu.pollution_analyzer.rec.soil_module.body",
                    List.of(
                            dominantSoil.getName(),
                            String.valueOf(dominantSoil.getBlockX()),
                            String.valueOf(dominantSoil.getBlockY()),
                            String.valueOf(dominantSoil.getBlockZ()),
                            formatRate("soil", dominantSoil.getSoilCurrentContribution())
                    )
            ));
        }

        PollutionAnalyzerSourceSnapshot dominantCombined = sources.stream()
                .max(Comparator.comparingDouble(PollutionAnalyzerSourceSnapshot::getTotalCurrentContribution))
                .orElse(null);

        PollutionAnalyzerChunkSnapshot bestTarget = findBestRelocationTarget(chunks, dominantCombined);
        if (dominantCombined != null && bestTarget != null &&
                (bestTarget.getChunkX() != dominantCombined.getChunkX() || bestTarget.getChunkZ() != dominantCombined.getChunkZ())) {
            recommendations.add(new PollutionAnalyzerRecommendation(
                    2,
                    "iu.pollution_analyzer.rec.move_source.title",
                    "iu.pollution_analyzer.rec.move_source.body",
                    List.of(
                            dominantCombined.getName(),
                            String.valueOf(bestTarget.getChunkX()),
                            String.valueOf(bestTarget.getChunkZ())
                    )
            ));
        }

        Map<Long, Long> sourcesPerChunk = sources.stream().collect(Collectors.groupingBy(
                source -> ChunkPos.asLong(source.getChunkX(), source.getChunkZ()),
                LinkedHashMap::new,
                Collectors.counting()
        ));

        for (Map.Entry<Long, Long> entry : sourcesPerChunk.entrySet()) {
            if (entry.getValue() >= 3L) {
                ChunkPos pos = new ChunkPos(entry.getKey());
                recommendations.add(new PollutionAnalyzerRecommendation(
                        1,
                        "iu.pollution_analyzer.rec.split_cluster.title",
                        "iu.pollution_analyzer.rec.split_cluster.body",
                        List.of(
                                String.valueOf(pos.x),
                                String.valueOf(pos.z),
                                String.valueOf(entry.getValue())
                        )
                ));
                break;
            }
        }

        PollutionAnalyzerChunkSnapshot fastRiskChunk = chunks.stream()
                .filter(chunk -> (chunk.getAirTimeToRiseSeconds() > 0 && chunk.getAirTimeToRiseSeconds() <= 180)
                        || (chunk.getSoilTimeToRiseSeconds() > 0 && chunk.getSoilTimeToRiseSeconds() <= 180))
                .max(Comparator.comparingDouble(chunk -> Math.max(chunk.getAirSeverity(), chunk.getSoilSeverity())))
                .orElse(null);

        if (fastRiskChunk != null) {
            int rise = fastRiskChunk.getAirTimeToRiseSeconds() > 0
                    ? fastRiskChunk.getAirTimeToRiseSeconds()
                    : fastRiskChunk.getSoilTimeToRiseSeconds();

            recommendations.add(new PollutionAnalyzerRecommendation(
                    2,
                    "iu.pollution_analyzer.rec.fast_rise.title",
                    "iu.pollution_analyzer.rec.fast_rise.body",
                    List.of(
                            String.valueOf(fastRiskChunk.getChunkX()),
                            String.valueOf(fastRiskChunk.getChunkZ()),
                            String.valueOf(rise)
                    )
            ));
        }

        recommendations.sort(Comparator.comparingInt(PollutionAnalyzerRecommendation::getSeverity).reversed());
        return recommendations;
    }

    private static boolean shouldRecommendAirCleaner(
            PollutionAnalyzerChunkSnapshot worstAirChunk,
            PollutionAnalyzerSourceSnapshot dominantAir,
            boolean hasAirCleanerInstalled
    ) {
        if (dominantAir == null && worstAirChunk == null) {
            return false;
        }

        if (!hasAirCleanerInstalled && dominantAir != null) {
            return true;
        }

        if (worstAirChunk == null) {
            return false;
        }

        return worstAirChunk.getAirSeverity() >= PollutionAnalyzerMath.airThresholds().getWarningFrom()
                || worstAirChunk.getAirRatePerSecond() > 0.05D;
    }

    private static boolean shouldRecommendSoilCleaner(
            PollutionAnalyzerChunkSnapshot worstSoilChunk,
            PollutionAnalyzerSourceSnapshot dominantSoil,
            boolean hasSoilCleanerInstalled
    ) {
        if (dominantSoil == null && worstSoilChunk == null) {
            return false;
        }

        if (!hasSoilCleanerInstalled && dominantSoil != null) {
            return true;
        }

        if (worstSoilChunk == null) {
            return false;
        }

        return worstSoilChunk.getSoilSeverity() >= PollutionAnalyzerMath.soilThresholds().getWarningFrom()
                || worstSoilChunk.getSoilRatePerSecond() > 0.05D;
    }

    private static double requiredAirCompensation(
            PollutionAnalyzerChunkSnapshot worstAirChunk,
            PollutionAnalyzerSourceSnapshot dominantAir
    ) {
        double chunkRate = worstAirChunk != null ? Math.max(0.0D, worstAirChunk.getAirRatePerSecond()) : 0.0D;
        double sourceRate = dominantAir != null ? Math.max(0.0D, dominantAir.getAirCurrentContribution()) : 0.0D;
        return Math.max(chunkRate, sourceRate);
    }

    private static double requiredSoilCompensation(
            PollutionAnalyzerChunkSnapshot worstSoilChunk,
            PollutionAnalyzerSourceSnapshot dominantSoil
    ) {
        double chunkRate = worstSoilChunk != null ? Math.max(0.0D, worstSoilChunk.getSoilRatePerSecond()) : 0.0D;
        double sourceRate = dominantSoil != null ? Math.max(0.0D, dominantSoil.getSoilCurrentContribution()) : 0.0D;
        return Math.max(chunkRate, sourceRate);
    }

    private static PollutionAnalyzerChunkSnapshot findBestRelocationTarget(
            List<PollutionAnalyzerChunkSnapshot> chunks,
            PollutionAnalyzerSourceSnapshot source
    ) {
        if (source == null) {
            return null;
        }

        return chunks.stream()
                .filter(chunk -> chunk.getChunkX() != source.getChunkX() || chunk.getChunkZ() != source.getChunkZ())
                .min(Comparator.comparingDouble(chunk ->
                        Math.max(chunk.getAirForecastFiveMinutes(), chunk.getSoilForecastFiveMinutes()) + chunk.getRiskScore() * 100.0F
                ))
                .orElse(null);
    }

    private static double[] expandSecondSeriesToTicks(double start, double[] seriesSeconds, int ticks) {
        double[] result = new double[ticks];
        if (ticks <= 0) {
            return result;
        }

        double prev = start;
        for (int tick = 0; tick < ticks; tick++) {
            int secondIndex = Math.min(seriesSeconds.length - 1, tick / 20);
            double target = seriesSeconds[secondIndex];
            double local = (tick % 20) / 20.0D;
            result[tick] = lerp(prev, target, local);
            if ((tick % 20) == 19) {
                prev = target;
            }
        }

        return result;
    }

    private static double[] buildRateSeries(double[] totalSeries) {
        double[] result = new double[totalSeries.length];
        if (totalSeries.length == 0) {
            return result;
        }

        for (int i = 0; i < totalSeries.length; i++) {
            int next = Math.min(totalSeries.length - 1, i + 20);
            result[i] = totalSeries[next] - totalSeries[i];
        }
        return result;
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double getAirCollectorCleaningPerSecond(int level) {
        int safeLevel = Math.max(0, Math.min(MAX_MECHANISM_LEVEL, level));
        return (5.0D * (1.0D + safeLevel)) / 3.0D;
    }

    private static double getSoilPurifierCleaningPerSecond(int level) {
        int safeLevel = Math.max(0, Math.min(MAX_MECHANISM_LEVEL, level));
        return 10.0D + safeLevel * 50.0D;
    }

    private static double getSoilPurifierAirSideEffectPerSecond(int level) {
        int safeLevel = Math.max(0, Math.min(MAX_MECHANISM_LEVEL, level));
        return 25.0D + safeLevel * 25.0D;
    }

    private static String formatRate(String medium, double value) {
        if ("air".equals(medium)) {
            return String.format(Locale.ROOT, "%+.2f " + Localization.translate("iu.pollution_analyzer.kg_per_sec_unit"), value);
        }
        return String.format(Locale.ROOT, "%+.2f " + Localization.translate("iu.pollution_analyzer.g_per_sec_unit"), value);
    }
}