package com.denfop.api.pollution.layer;

import com.denfop.IUItem;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.pollution.utils.Vec2f;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.blocks.RadiationDustBlock;
import com.denfop.blocks.SootBlock;
import com.denfop.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;

public final class EnvironmentalLayerManager {

    private static final float MIN_SOOT_SEVERITY = 0.08F;
    private static final float MIN_RAD_SEVERITY = 0.08F;
    private static final float MIN_AIR_GROWTH_SEVERITY = 0.10F;
    private static final float MIN_RADIATION_EROSION_SEVERITY = 0.55F;

    private static final float CORE_WEIGHT = 1.00F;
    private static final float FIRST_RING_CARDINAL_WEIGHT = 0.32F;
    private static final float FIRST_RING_DIAGONAL_WEIGHT = 0.22F;
    private static final float SECOND_RING_CARDINAL_WEIGHT = 0.14F;
    private static final float SECOND_RING_DIAGONAL_WEIGHT = 0.08F;
    private static final int MAX_LAYER_SPREAD_CHUNKS = 1;

    private EnvironmentalLayerManager() {
    }

    public static void tick(ServerLevel level) {
        if (level == null || level.isClientSide || level.getGameTime() % 20 != 0L) {
            return;
        }

        processAirToxicOvergrowth(level);
        processRadiationErosion(level);

        if (!(level.isRaining() || level.isThundering())) {
            return;
        }

        processSoot(level);
        processRadiationDust(level);
    }

    private static void addContributionLimited(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            int chunkX,
            int chunkZ,
            float contribution,
            boolean core,
            int maxRadius
    ) {
        if (contribution <= 0.0F) {
            return;
        }

        if (Math.abs(chunkX - sourceChunk.x) > maxRadius || Math.abs(chunkZ - sourceChunk.z) > maxRadius) {
            return;
        }

        addContribution(targets, chunkX, chunkZ, contribution, core);
    }

    private static void addSymmetricRingLimited(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            boolean radiation,
            int maxRadius
    ) {
        float cardinalWeight = radiation ? 0.36F : FIRST_RING_CARDINAL_WEIGHT;
        float diagonalWeight = radiation ? 0.26F : FIRST_RING_DIAGONAL_WEIGHT;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }

                boolean diagonal = dx != 0 && dz != 0;
                float weight = diagonal ? diagonalWeight : cardinalWeight;

                addContributionLimited(
                        targets,
                        sourceChunk,
                        coreChunk.x + dx,
                        coreChunk.z + dz,
                        severity * weight * ringFactor,
                        false,
                        maxRadius
                );
            }
        }
    }

    private static void addWindBiasedFirstRingLimited(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            int dirX,
            int dirZ,
            int maxRadius
    ) {
        int leftX = -dirZ;
        int leftZ = dirX;
        int rightX = dirZ;
        int rightZ = -dirX;

        addContributionLimited(
                targets,
                sourceChunk,
                coreChunk.x + dirX,
                coreChunk.z + dirZ,
                severity * 0.46F * ringFactor,
                false,
                maxRadius
        );

        addContributionLimited(
                targets,
                sourceChunk,
                coreChunk.x + leftX,
                coreChunk.z + leftZ,
                severity * 0.22F * ringFactor,
                false,
                maxRadius
        );
        addContributionLimited(
                targets,
                sourceChunk,
                coreChunk.x + rightX,
                coreChunk.z + rightZ,
                severity * 0.22F * ringFactor,
                false,
                maxRadius
        );

        addContributionLimited(
                targets,
                sourceChunk,
                coreChunk.x + dirX + leftX,
                coreChunk.z + dirZ + leftZ,
                severity * 0.28F * ringFactor,
                false,
                maxRadius
        );
        addContributionLimited(
                targets,
                sourceChunk,
                coreChunk.x + dirX + rightX,
                coreChunk.z + dirZ + rightZ,
                severity * 0.28F * ringFactor,
                false,
                maxRadius
        );

        if (severity >= 0.40F) {
            addContributionLimited(
                    targets,
                    sourceChunk,
                    coreChunk.x - dirX,
                    coreChunk.z - dirZ,
                    severity * 0.08F * ringFactor,
                    false,
                    maxRadius
            );
        }
    }

    private static void processSoot(ServerLevel level) {
        if (!ModConfig.COMMON.airPollution.get()) {
            return;
        }

        PollutionManager pollutionManager = PollutionManager.pollutionManager;
        if (pollutionManager == null) {
            return;
        }

        Map<ChunkPos, ChunkLevel> airMap = pollutionManager.getPollutionAirMap();
        if (airMap.isEmpty()) {
            return;
        }

        Vec2f windVector = WindSystem.windSystem != null
                ? pollutionManager.getVector(WindSystem.windSystem.getWindSide())
                : new Vec2f(0, 0);

        long stepTime = level.getGameTime() / 20L;
        boolean wetWorld = level.isRaining();
        Block sootBlock = IUItem.sootBlock.getBlock(SootBlock.Type.soot).get();

        Map<Long, AccumulatedLayerTarget> targets = new HashMap<>();

        for (Map.Entry<ChunkPos, ChunkLevel> entry : airMap.entrySet()) {
            ChunkPos sourceChunk = entry.getKey();
            ChunkLevel airLevel = entry.getValue();

            float severity = computeSootSeverity(airLevel);
            if (severity < MIN_SOOT_SEVERITY) {
                continue;
            }

            accumulateSootTargets(targets, sourceChunk, severity, windVector);
        }

        flushAccumulatedLayerTargets(level, sootBlock, targets, stepTime, wetWorld);
    }

    private static void processRadiationDust(ServerLevel level) {
        if (!ModConfig.COMMON.radiationChunksEnabled.get()) {
            return;
        }

        RadiationSystem radiationSystem = RadiationSystem.rad_system;
        if (radiationSystem == null || radiationSystem.getMap().isEmpty()) {
            return;
        }

        long stepTime = level.getGameTime() / 20L;
        boolean wetWorld = level.isRaining();
        Block radiationDustBlock = IUItem.radiationDustBlock.getBlock(RadiationDustBlock.Type.radiation_dust).get();

        Map<Long, AccumulatedLayerTarget> targets = new HashMap<>();

        for (Radiation radiation : radiationSystem.getMap().values()) {
            if (radiation == null || radiation.getPos() == null) {
                continue;
            }

            float severity = computeRadiationSeverity(radiation);
            if (severity < MIN_RAD_SEVERITY) {
                continue;
            }

            accumulateRadiationTargets(targets, radiation.getPos(), severity, radiation.getLevel().ordinal());
        }

        flushAccumulatedLayerTargets(level, radiationDustBlock, targets, stepTime, wetWorld);
    }

    private static void processAirToxicOvergrowth(ServerLevel level) {
        if (!ModConfig.COMMON.airPollution.get()) {
            return;
        }
        if (level.isRaining() || level.isThundering()) {
            return;
        }
        PollutionManager pollutionManager = PollutionManager.pollutionManager;
        if (pollutionManager == null) {
            return;
        }

        Map<ChunkPos, ChunkLevel> airMap = pollutionManager.getPollutionAirMap();
        if (airMap.isEmpty()) {
            return;
        }

        Vec2f windVector = WindSystem.windSystem != null
                ? pollutionManager.getVector(WindSystem.windSystem.getWindSide())
                : new Vec2f(0, 0);

        long stepTime = level.getGameTime() / 20L;
        boolean wetWorld = level.isRaining() || level.isThundering();

        Map<Long, AccumulatedLayerTarget> targets = new HashMap<>();

        for (Map.Entry<ChunkPos, ChunkLevel> entry : airMap.entrySet()) {
            ChunkPos sourceChunk = entry.getKey();
            ChunkLevel airLevel = entry.getValue();

            float severity = computeAirGrowthSeverity(airLevel);
            if (severity < MIN_AIR_GROWTH_SEVERITY) {
                continue;
            }

            accumulateAirGrowthTargets(targets, sourceChunk, severity, windVector);
        }

        flushAccumulatedToxicGrowthTargets(level, targets, stepTime, wetWorld);
    }

    private static void processRadiationErosion(ServerLevel level) {
        if (!ModConfig.COMMON.radiationChunksEnabled.get()) {
            return;
        }

        RadiationSystem radiationSystem = RadiationSystem.rad_system;
        if (radiationSystem == null || radiationSystem.getMap().isEmpty()) {
            return;
        }

        long stepTime = level.getGameTime() / 20L;
        Map<Long, AccumulatedLayerTarget> targets = new HashMap<>();

        for (Radiation radiation : radiationSystem.getMap().values()) {
            if (radiation == null || radiation.getPos() == null) {
                continue;
            }

            float severity = computeRadiationSeverity(radiation);
            if (severity < MIN_RADIATION_EROSION_SEVERITY) {
                continue;
            }

            accumulateRadiationErosionTargets(targets, radiation.getPos(), severity, radiation.getLevel().ordinal());
        }

        flushAccumulatedRadiationErosionTargets(level, targets, stepTime);
    }

    private static void accumulateSootTargets(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            float severity,
            Vec2f windVector
    ) {

        addContributionLimited(
                targets,
                sourceChunk,
                sourceChunk.x,
                sourceChunk.z,
                severity * CORE_WEIGHT,
                true,
                MAX_LAYER_SPREAD_CHUNKS
        );

        float ringFactor = smoothIntensity(severity);
        if (ringFactor <= 0.01F) {
            return;
        }

        int dirX = sign(windVector.x);
        int dirZ = sign(windVector.y);

        if (dirX == 0 && dirZ == 0) {
            addSymmetricRingLimited(
                    targets,
                    sourceChunk,
                    sourceChunk,
                    severity,
                    ringFactor,
                    false,
                    MAX_LAYER_SPREAD_CHUNKS
            );
        } else {
            addWindBiasedFirstRingLimited(
                    targets,
                    sourceChunk,
                    sourceChunk,
                    severity,
                    ringFactor,
                    dirX,
                    dirZ,
                    MAX_LAYER_SPREAD_CHUNKS
            );
        }


    }

    private static void accumulateRadiationTargets(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            float severity,
            int radiationOrdinal
    ) {
        addContributionLimited(
                targets,
                sourceChunk,
                sourceChunk.x,
                sourceChunk.z,
                severity * CORE_WEIGHT,
                true,
                MAX_LAYER_SPREAD_CHUNKS
        );

        float ringFactor = smoothIntensity(severity);
        if (ringFactor <= 0.01F) {
            return;
        }

        addSymmetricRingLimited(
                targets,
                sourceChunk,
                sourceChunk,
                severity,
                ringFactor,
                true,
                MAX_LAYER_SPREAD_CHUNKS
        );


    }

    private static void accumulateAirGrowthTargets(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            float severity,
            Vec2f windVector
    ) {
        ChunkPos coreChunk = sourceChunk;

        addContribution(targets, coreChunk.x, coreChunk.z, severity * CORE_WEIGHT, true);

        float ringFactor = smoothIntensity(severity);
        if (ringFactor <= 0.01F) {
            return;
        }

        int dirX = sign(windVector.x);
        int dirZ = sign(windVector.y);

        if (dirX == 0 && dirZ == 0) {
            addSymmetricRing(targets, coreChunk, severity, ringFactor * 0.90F, false);
        } else {
            addWindBiasedFirstRing(targets, coreChunk, severity, ringFactor * 0.92F, dirX, dirZ);
        }

        if (severity >= 0.45F) {
            float secondRingFactor = Math.max(0.0F, (severity - 0.45F) / 0.55F);
            addWindBiasedSecondRing(targets, coreChunk, severity, secondRingFactor * 0.85F, dirX, dirZ);
        }
    }

    private static void accumulateRadiationErosionTargets(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos sourceChunk,
            float severity,
            int radiationOrdinal
    ) {
        addContribution(targets, sourceChunk.x, sourceChunk.z, severity * 1.15F, true);

        float ringFactor = smoothIntensity(severity);
        if (ringFactor <= 0.01F) {
            return;
        }

        float cardinal = severity * 0.24F * ringFactor;
        float diagonal = severity * 0.14F * ringFactor;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }

                boolean isDiagonal = dx != 0 && dz != 0;
                addContribution(
                        targets,
                        sourceChunk.x + dx,
                        sourceChunk.z + dz,
                        isDiagonal ? diagonal : cardinal,
                        false
                );
            }
        }

        if (radiationOrdinal >= 4 || severity >= 0.85F) {
            float secondRingFactor = Math.max(0.0F, (severity - 0.75F) / 0.25F);

            addContribution(targets, sourceChunk.x + 2, sourceChunk.z, severity * 0.10F * secondRingFactor, false);
            addContribution(targets, sourceChunk.x - 2, sourceChunk.z, severity * 0.10F * secondRingFactor, false);
            addContribution(targets, sourceChunk.x, sourceChunk.z + 2, severity * 0.10F * secondRingFactor, false);
            addContribution(targets, sourceChunk.x, sourceChunk.z - 2, severity * 0.10F * secondRingFactor, false);
        }
    }

    private static void addSymmetricRing(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            boolean radiation
    ) {
        float cardinalWeight = radiation ? 0.36F : FIRST_RING_CARDINAL_WEIGHT;
        float diagonalWeight = radiation ? 0.26F : FIRST_RING_DIAGONAL_WEIGHT;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }

                boolean diagonal = dx != 0 && dz != 0;
                float weight = diagonal ? diagonalWeight : cardinalWeight;
                addContribution(
                        targets,
                        coreChunk.x + dx,
                        coreChunk.z + dz,
                        severity * weight * ringFactor,
                        false
                );
            }
        }
    }

    private static void addSecondSymmetricRing(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            boolean radiation
    ) {
        if (ringFactor <= 0.01F) {
            return;
        }

        float cardinalWeight = radiation ? 0.18F : SECOND_RING_CARDINAL_WEIGHT;
        float diagonalWeight = radiation ? 0.10F : SECOND_RING_DIAGONAL_WEIGHT;

        addContribution(targets, coreChunk.x + 2, coreChunk.z, severity * cardinalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x - 2, coreChunk.z, severity * cardinalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x, coreChunk.z + 2, severity * cardinalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x, coreChunk.z - 2, severity * cardinalWeight * ringFactor, false);

        addContribution(targets, coreChunk.x + 2, coreChunk.z + 1, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x + 2, coreChunk.z - 1, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x - 2, coreChunk.z + 1, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x - 2, coreChunk.z - 1, severity * diagonalWeight * ringFactor, false);

        addContribution(targets, coreChunk.x + 1, coreChunk.z + 2, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x + 1, coreChunk.z - 2, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x - 1, coreChunk.z + 2, severity * diagonalWeight * ringFactor, false);
        addContribution(targets, coreChunk.x - 1, coreChunk.z - 2, severity * diagonalWeight * ringFactor, false);
    }

    private static void addWindBiasedFirstRing(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            int dirX,
            int dirZ
    ) {
        int leftX = -dirZ;
        int leftZ = dirX;
        int rightX = dirZ;
        int rightZ = -dirX;

        addContribution(targets, coreChunk.x + dirX, coreChunk.z + dirZ, severity * 0.46F * ringFactor, false);

        addContribution(targets, coreChunk.x + leftX, coreChunk.z + leftZ, severity * 0.22F * ringFactor, false);
        addContribution(targets, coreChunk.x + rightX, coreChunk.z + rightZ, severity * 0.22F * ringFactor, false);

        addContribution(
                targets,
                coreChunk.x + dirX + leftX,
                coreChunk.z + dirZ + leftZ,
                severity * 0.28F * ringFactor,
                false
        );
        addContribution(
                targets,
                coreChunk.x + dirX + rightX,
                coreChunk.z + dirZ + rightZ,
                severity * 0.28F * ringFactor,
                false
        );

        if (severity >= 0.40F) {
            addContribution(targets, coreChunk.x - dirX, coreChunk.z - dirZ, severity * 0.08F * ringFactor, false);
        }
    }

    private static void addWindBiasedSecondRing(
            Map<Long, AccumulatedLayerTarget> targets,
            ChunkPos coreChunk,
            float severity,
            float ringFactor,
            int dirX,
            int dirZ
    ) {
        if (ringFactor <= 0.01F) {
            return;
        }

        int leftX = -dirZ;
        int leftZ = dirX;
        int rightX = dirZ;
        int rightZ = -dirX;

        addContribution(
                targets,
                coreChunk.x + dirX * 2,
                coreChunk.z + dirZ * 2,
                severity * 0.20F * ringFactor,
                false
        );

        addContribution(
                targets,
                coreChunk.x + dirX * 2 + leftX,
                coreChunk.z + dirZ * 2 + leftZ,
                severity * 0.12F * ringFactor,
                false
        );
        addContribution(
                targets,
                coreChunk.x + dirX * 2 + rightX,
                coreChunk.z + dirZ * 2 + rightZ,
                severity * 0.12F * ringFactor,
                false
        );

        addContribution(
                targets,
                coreChunk.x + leftX * 2,
                coreChunk.z + leftZ * 2,
                severity * 0.08F * ringFactor,
                false
        );
        addContribution(
                targets,
                coreChunk.x + rightX * 2,
                coreChunk.z + rightZ * 2,
                severity * 0.08F * ringFactor,
                false
        );
    }

    private static void flushAccumulatedLayerTargets(
            ServerLevel level,
            Block layerBlock,
            Map<Long, AccumulatedLayerTarget> targets,
            long stepTime,
            boolean wetWorld
    ) {
        if (targets.isEmpty()) {
            return;
        }

        for (AccumulatedLayerTarget target : targets.values()) {
            float severity = target.resolveSeverity();
            if (severity <= 0.0F) {
                continue;
            }

            if (!level.hasChunk(target.chunkX, target.chunkZ)) {
                continue;
            }

            int interval = getIntervalBySeverity(severity, wetWorld);
            int attempts = getAttemptsBySeverity(severity) + (target.coreHits > 0 ? 1 : 0);
            float chance = getChanceBySeverity(severity, wetWorld);

            int hash = Math.floorMod(target.chunkX * 73428767 + target.chunkZ * 912931, Math.max(1, interval));
            if ((stepTime + hash) % interval != 0) {
                continue;
            }

            ChunkPos targetChunk = new ChunkPos(target.chunkX, target.chunkZ);

            for (int i = 0; i < attempts; i++) {
                EnvironmentalLayerHelper.tryAccumulate(level, targetChunk, layerBlock, chance, true);
            }
        }
    }

    private static void flushAccumulatedToxicGrowthTargets(
            ServerLevel level,
            Map<Long, AccumulatedLayerTarget> targets,
            long stepTime,
            boolean wetWorld
    ) {
        if (targets.isEmpty()) {
            return;
        }

        for (AccumulatedLayerTarget target : targets.values()) {
            float severity = target.resolveSeverity();
            if (severity < MIN_AIR_GROWTH_SEVERITY) {
                continue;
            }

            if (!level.hasChunk(target.chunkX, target.chunkZ)) {
                continue;
            }

            int interval = getOvergrowthIntervalBySeverity(severity, wetWorld);
            int attempts = getOvergrowthAttemptsBySeverity(severity) + (target.coreHits > 0 ? 1 : 0);
            float chance = getOvergrowthChanceBySeverity(severity, wetWorld);

            int hash = Math.floorMod(target.chunkX * 1610612741 + target.chunkZ * 805306457, Math.max(1, interval));
            if ((stepTime + hash) % interval != 0) {
                continue;
            }

            ChunkPos targetChunk = new ChunkPos(target.chunkX, target.chunkZ);

            for (int i = 0; i < attempts; i++) {
                tryGrowToxicOvergrowth(level, targetChunk, chance, severity);
            }
        }
    }

    private static void flushAccumulatedRadiationErosionTargets(
            ServerLevel level,
            Map<Long, AccumulatedLayerTarget> targets,
            long stepTime
    ) {
        if (targets.isEmpty()) {
            return;
        }

        for (AccumulatedLayerTarget target : targets.values()) {
            float severity = target.resolveSeverity();
            if (severity < MIN_RADIATION_EROSION_SEVERITY) {
                continue;
            }

            if (!level.hasChunk(target.chunkX, target.chunkZ)) {
                continue;
            }

            int interval = getRadiationErosionIntervalBySeverity(severity);
            int attempts = getRadiationErosionAttemptsBySeverity(severity) + (target.coreHits > 0 ? 1 : 0);
            float chance = getRadiationErosionChanceBySeverity(severity);

            int hash = Math.floorMod(target.chunkX * 3571 + target.chunkZ * 2377, Math.max(1, interval));
            if ((stepTime + hash) % interval != 0) {
                continue;
            }

            ChunkPos targetChunk = new ChunkPos(target.chunkX, target.chunkZ);

            for (int i = 0; i < attempts; i++) {
                if (level.random.nextFloat() > chance) {
                    continue;
                }
                tryRadiationErode(level, targetChunk, severity);
            }
        }
    }

    private static void tryGrowToxicOvergrowth(ServerLevel level, ChunkPos chunkPos, float chance, float severity) {
        if (level.random.nextFloat() > chance) {
            return;
        }

        boolean placeVines = level.random.nextFloat() < getVineChance(severity);
        if (placeVines) {
            if (tryPlaceVine(level, chunkPos, severity)) {
                return;
            }
            tryPlaceMoss(level, chunkPos, severity);
            return;
        }

        if (tryPlaceMoss(level, chunkPos, severity)) {
            if (level.random.nextFloat() < 0.28F + severity * 0.22F) {
                tryPlaceAdjacentVineNearGround(level, chunkPos);
            }
            return;
        }

        tryPlaceVine(level, chunkPos, severity);
    }

    private static boolean tryPlaceMoss(ServerLevel level, ChunkPos chunkPos, float severity) {
        for (int attempt = 0; attempt < 2; attempt++) {
            int x = (chunkPos.x << 4) + level.random.nextInt(16);
            int z = (chunkPos.z << 4) + level.random.nextInt(16);

            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
            if (y < level.getMinBuildHeight()) {
                continue;
            }

            BlockPos groundPos = new BlockPos(x, y, z);
            BlockPos topPos = groundPos.above();

            BlockState groundState = level.getBlockState(groundPos);
            BlockState topState = level.getBlockState(topPos);

            if (!topState.isAir() || !topState.getFluidState().isEmpty()) {
                continue;
            }

            if (!canGrowMossOn(groundState, level, groundPos)) {
                continue;
            }

            level.setBlock(topPos, Blocks.MOSS_CARPET.defaultBlockState(), 3);

            if (severity >= 0.55F && level.random.nextFloat() < 0.22F + severity * 0.18F) {
                trySpreadAdjacentMoss(level, topPos, severity);
            }
            return true;
        }

        return false;
    }

    private static void trySpreadAdjacentMoss(ServerLevel level, BlockPos centerTopPos, float severity) {
        int extra = severity >= 0.80F ? 3 : 2;

        for (int i = 0; i < extra; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
            BlockPos nextTop = centerTopPos.relative(dir);
            BlockPos nextGround = nextTop.below();

            BlockState nextGroundState = level.getBlockState(nextGround);
            BlockState nextTopState = level.getBlockState(nextTop);

            if (!nextTopState.isAir() || !nextTopState.getFluidState().isEmpty()) {
                continue;
            }

            if (!canGrowMossOn(nextGroundState, level, nextGround)) {
                continue;
            }

            level.setBlock(nextTop, Blocks.MOSS_CARPET.defaultBlockState(), 3);
        }
    }

    private static boolean tryPlaceVine(ServerLevel level, ChunkPos chunkPos, float severity) {
        for (int attempt = 0; attempt < 2; attempt++) {
            int x = (chunkPos.x << 4) + level.random.nextInt(16);
            int z = (chunkPos.z << 4) + level.random.nextInt(16);

            int topY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
            int minY = Math.max(level.getMinBuildHeight() + 1, topY - 8);
            int maxY = Math.min(level.getMaxBuildHeight() - 2, topY + 3);

            if (maxY <= minY) {
                continue;
            }

            int y = Mth.nextInt(level.random, minY, maxY);
            BlockPos vinePos = new BlockPos(x, y, z);

            if (!level.getBlockState(vinePos).isAir()) {
                continue;
            }

            VinePlacement placement = findVinePlacement(level, vinePos);
            if (placement == null) {
                continue;
            }

            BlockState vineState = Blocks.VINE.defaultBlockState()
                    .setValue(VineBlock.getPropertyForFace(placement.face), true);

            if (!vineState.canSurvive(level, vinePos)) {
                continue;
            }

            level.setBlock(vinePos, vineState, 3);

            int extraLength = severity >= 0.75F ? 2 : (severity >= 0.45F ? 1 : 0);
            BlockPos downPos = vinePos.below();
            for (int i = 0; i < extraLength; i++) {
                if (!level.getBlockState(downPos).isAir()) {
                    break;
                }

                BlockState downState = Blocks.VINE.defaultBlockState()
                        .setValue(VineBlock.getPropertyForFace(placement.face), true);

                if (!downState.canSurvive(level, downPos)) {
                    break;
                }

                level.setBlock(downPos, downState, 3);
                downPos = downPos.below();
            }

            if (level.random.nextFloat() < 0.20F + severity * 0.16F) {
                tryPlaceNearbyExtraVine(level, vinePos, placement.face);
            }
            return true;
        }

        return false;
    }

    private static void tryPlaceNearbyExtraVine(ServerLevel level, BlockPos sourcePos, Direction sourceFace) {
        Direction side = level.random.nextBoolean()
                ? sourceFace.getClockWise()
                : sourceFace.getCounterClockWise();

        BlockPos newPos = sourcePos.relative(side);
        if (!level.getBlockState(newPos).isAir()) {
            return;
        }

        VinePlacement placement = findVinePlacement(level, newPos);
        if (placement == null) {
            return;
        }

        BlockState vineState = Blocks.VINE.defaultBlockState()
                .setValue(VineBlock.getPropertyForFace(placement.face), true);

        if (vineState.canSurvive(level, newPos)) {
            level.setBlock(newPos, vineState, 3);
        }
    }

    private static void tryPlaceAdjacentVineNearGround(ServerLevel level, ChunkPos chunkPos) {
        int x = (chunkPos.x << 4) + level.random.nextInt(16);
        int z = (chunkPos.z << 4) + level.random.nextInt(16);

        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos vinePos = new BlockPos(x, y, z);

        if (!level.getBlockState(vinePos).isAir()) {
            return;
        }

        VinePlacement placement = findVinePlacement(level, vinePos);
        if (placement == null) {
            return;
        }

        BlockState vineState = Blocks.VINE.defaultBlockState()
                .setValue(VineBlock.getPropertyForFace(placement.face), true);

        if (vineState.canSurvive(level, vinePos)) {
            level.setBlock(vinePos, vineState, 3);
        }
    }

    private static void tryRadiationErode(ServerLevel level, ChunkPos chunkPos, float severity) {
        int x = (chunkPos.x << 4) + level.random.nextInt(16);
        int z = (chunkPos.z << 4) + level.random.nextInt(16);

        if (!tryErodeTopColumn(level, x, z)) {
            return;
        }

        if (severity < 0.68F) {
            return;
        }

        int sideRemovals = severity >= 0.88F ? 2 : 1;
        for (int i = 0; i < sideRemovals; i++) {
            if (level.random.nextFloat() > (0.30F + severity * 0.25F)) {
                continue;
            }

            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
            int nx = x + dir.getStepX();
            int nz = z + dir.getStepZ();
            tryErodeTopColumn(level, nx, nz);
        }
    }

    private static boolean tryErodeTopColumn(ServerLevel level, int x, int z) {
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
        if (surfaceY < level.getMinBuildHeight()) {
            return false;
        }

        int minY = Math.max(level.getMinBuildHeight() + 1, surfaceY - 32);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int y = surfaceY; y >= minY; y--) {
            cursor.set(x, y, z);
            BlockState state = level.getBlockState(cursor);

            if (state.isAir() || !state.getFluidState().isEmpty()) {
                continue;
            }

            if (isProtectedFromRadiationErosion(level, cursor, state)) {
                return false;
            }

            if (!canBeRadiationEroded(level, cursor, state)) {
                return false;
            }

            level.levelEvent(2001, cursor, Block.getId(state));
            level.removeBlock(cursor, false);
            return true;
        }

        return false;
    }

    private static boolean isProtectedFromRadiationErosion(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.hasBlockEntity() || level.getBlockEntity(pos) != null) {
            return true;
        }

        return state.is(Blocks.BEDROCK)
                || state.is(Blocks.BARRIER)
                || state.is(Blocks.END_PORTAL)
                || state.is(Blocks.END_PORTAL_FRAME)
                || state.is(Blocks.COMMAND_BLOCK)
                || state.is(Blocks.CHAIN_COMMAND_BLOCK)
                || state.is(Blocks.REPEATING_COMMAND_BLOCK)
                || state.is(Blocks.STRUCTURE_BLOCK)
                || state.is(Blocks.JIGSAW)
                || state.is(Blocks.STRUCTURE_VOID)
                || state.is(Blocks.LIGHT);
    }

    private static boolean canBeRadiationEroded(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.isAir() || !state.getFluidState().isEmpty()) {
            return false;
        }

        float destroySpeed = state.getDestroySpeed(level, pos);
        if (destroySpeed < 0.0F) {
            return false;
        }

        return true;
    }

    private static VinePlacement findVinePlacement(ServerLevel level, BlockPos vinePos) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            BlockPos supportPos = vinePos.relative(face.getOpposite());
            BlockState supportState = level.getBlockState(supportPos);

            if (canSupportToxicVine(supportState, level, supportPos, face)) {
                return new VinePlacement(face);
            }
        }
        return null;
    }

    private static boolean canGrowMossOn(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.isAir() || !state.getFluidState().isEmpty()) {
            return false;
        }

        if (!state.isFaceSturdy(level, pos, Direction.UP)) {
            return false;
        }

        return state.is(BlockTags.DIRT)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(Blocks.STONE)
                || state.is(Blocks.COBBLESTONE)
                || state.is(Blocks.MOSS_BLOCK)
                || state.is(Blocks.MOSS_CARPET)
                || state.is(Blocks.ANDESITE)
                || state.is(Blocks.DIORITE)
                || state.is(Blocks.GRANITE)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.MUD)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.GRAVEL)
                || state.is(BlockTags.LOGS);
    }

    private static boolean canSupportToxicVine(BlockState state, ServerLevel level, BlockPos pos, Direction face) {
        if (state.isAir() || !state.getFluidState().isEmpty()) {
            return false;
        }

        return state.isFaceSturdy(level, pos, face)
                || state.is(BlockTags.LOGS)
                || state.is(Blocks.MOSS_BLOCK)
                || state.is(Blocks.COBBLESTONE)
                || state.is(Blocks.STONE_BRICKS)
                || state.is(Blocks.CRACKED_STONE_BRICKS)
                || state.is(Blocks.MOSSY_COBBLESTONE)
                || state.is(Blocks.MOSSY_STONE_BRICKS);
    }

    private static ChunkPos driftChunk(ChunkPos sourceChunk, Vec2f windVector, float severity) {
        int driftX = 0;
        int driftZ = 0;

        if (severity >= 0.35F) {
            driftX = sign(windVector.x);
            driftZ = sign(windVector.y);
        }

        return new ChunkPos(sourceChunk.x + driftX, sourceChunk.z + driftZ);
    }

    private static float computeSootSeverity(ChunkLevel airLevel) {
        if (airLevel == null) {
            return 0.0F;
        }

        float levelSeverity = airLevel.getLevelPollution().ordinal() * 0.22F;
        float storedSeverity = Mth.clamp((float) (airLevel.getPollution() / 500.0D), 0.0F, 1.0F) * 0.20F;

        return Mth.clamp(levelSeverity + storedSeverity, 0.0F, 1.0F);
    }

    private static float computeRadiationSeverity(Radiation radiation) {
        float levelSeverity = radiation.getLevel().ordinal() * 0.22F;
        float storedSeverity = Mth.clamp((float) (radiation.getRadiation() / 200.0D), 0.0F, 1.0F) * 0.20F;
        return Mth.clamp(levelSeverity + storedSeverity, 0.0F, 1.0F);
    }

    private static float computeAirGrowthSeverity(ChunkLevel airLevel) {
        if (airLevel == null) {
            return 0.0F;
        }

        float levelSeverity = airLevel.getLevelPollution().ordinal() * 0.24F;
        float storedSeverity = Mth.clamp((float) (airLevel.getPollution() / 450.0D), 0.0F, 1.0F) * 0.22F;
        return Mth.clamp(levelSeverity + storedSeverity, 0.0F, 1.0F);
    }

    private static int getIntervalBySeverity(float severity, boolean wetWorld) {
        int baseInterval;
        if (severity >= 0.80F) {
            baseInterval = 4;
        } else if (severity >= 0.55F) {
            baseInterval = 10;
        } else if (severity >= 0.30F) {
            baseInterval = 20;
        } else {
            baseInterval = 40;
        }

        return wetWorld ? Math.max(1, baseInterval - 1) : baseInterval;
    }

    private static int getAttemptsBySeverity(float severity) {
        if (severity >= 0.80F) {
            return 2;
        }
        if (severity >= 0.55F) {
            return 1;
        }
        if (severity >= 0.30F) {
            return 1;
        }
        if (severity >= 0.15F) {
            return 1;
        }
        return 1;
    }

    private static float getChanceBySeverity(float severity, boolean wetWorld) {
        float chance = 0.04F + severity * 0.16F;
        if (wetWorld) {
            chance += 0.04F;
        }
        return Mth.clamp(chance, 0.0F, 1.0F);
    }

    private static int getOvergrowthIntervalBySeverity(float severity, boolean wetWorld) {
        if (severity >= 0.80F) {
            return 60;
        }
        if (severity >= 0.55F) {
            return 100;
        }
        if (severity >= 0.30F) {
            return 160;
        }
        return 240;
    }


    private static int getOvergrowthAttemptsBySeverity(float severity) {
        if (severity >= 0.80F) {
            return 4;
        }
        if (severity >= 0.55F) {
            return 3;
        }
        if (severity >= 0.30F) {
            return 2;
        }
        return 1;
    }

    private static float getOvergrowthChanceBySeverity(float severity, boolean wetWorld) {
        float chance = 0.16F + severity * 0.42F;
        if (wetWorld) {
            chance += 0.12F;
        }
        return Mth.clamp(chance, 0.0F, 1.0F);
    }

    private static float getVineChance(float severity) {
        return Mth.clamp(0.32F + severity * 0.30F, 0.0F, 0.82F);
    }

    private static int getRadiationErosionIntervalBySeverity(float severity) {
        if (severity >= 0.90F) {
            return 1;
        }
        if (severity >= 0.80F) {
            return 2;
        }
        if (severity >= 0.70F) {
            return 3;
        }
        return 5;
    }

    private static int getRadiationErosionAttemptsBySeverity(float severity) {
        if (severity >= 0.90F) {
            return 4;
        }
        if (severity >= 0.80F) {
            return 3;
        }
        if (severity >= 0.70F) {
            return 2;
        }
        return 1;
    }

    private static float getRadiationErosionChanceBySeverity(float severity) {
        return Mth.clamp(0.24F + severity * 0.46F, 0.0F, 1.0F);
    }

    private static float smoothIntensity(float severity) {
        float t = Mth.clamp((severity - 0.08F) / 0.92F, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    private static int sign(float value) {
        return value > 0.0F ? 1 : (value < 0.0F ? -1 : 0);
    }

    private static long packChunk(int x, int z) {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

    private static void addContribution(
            Map<Long, AccumulatedLayerTarget> targets,
            int chunkX,
            int chunkZ,
            float contribution,
            boolean core
    ) {
        if (contribution <= 0.0F) {
            return;
        }

        long key = packChunk(chunkX, chunkZ);
        AccumulatedLayerTarget target = targets.get(key);
        if (target == null) {
            target = new AccumulatedLayerTarget(chunkX, chunkZ);
            targets.put(key, target);
        }
        target.add(contribution, core);
    }

    private static final class AccumulatedLayerTarget {
        private final int chunkX;
        private final int chunkZ;
        private float totalContribution;
        private float maxContribution;
        private int contributors;
        private int coreHits;

        private AccumulatedLayerTarget(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        private void add(float contribution, boolean core) {
            this.totalContribution += contribution;
            this.maxContribution = Math.max(this.maxContribution, contribution);
            this.contributors++;
            if (core) {
                this.coreHits++;
            }
        }

        private float resolveSeverity() {
            float severity = this.maxContribution * 0.58F
                    + this.totalContribution * 0.42F
                    + Math.min(0.10F, this.contributors * 0.015F)
                    + Math.min(0.12F, this.coreHits * 0.04F);

            return Mth.clamp(severity, 0.0F, 1.0F);
        }
    }

    private static final class VinePlacement {
        private final Direction face;

        private VinePlacement(Direction face) {
            this.face = face;
        }
    }
}
