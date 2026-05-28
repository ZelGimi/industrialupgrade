package com.denfop.world.vein;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.BlockDeposits2;
import com.denfop.config.ModConfig;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.noise.PerlinNoiseViewer;
import com.denfop.world.vein.noise.Point;
import com.denfop.world.vein.noise.ShellCluster;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static com.denfop.world.vein.VeinType.veinTypeMap;

public class AlgorithmVein extends Feature<NoneFeatureConfiguration> {

    public static List<VeinStructure> veinStructureList = new LinkedList<>();
    public static List<ShellCluster> shellClusterList = new ArrayList<>();
    public static ShellCluster volcano;
    public static Map<Integer, Map<Integer, Tuple<Color, Integer>>> shellClusterChuncks = new HashMap<>();
    public static Map<Integer, Map<Integer, List<Integer>>> veinCoordination = new HashMap<>();

    private static final int MAX_PENDING_CHUNKS_PER_TICK = 64;

    private static final ConcurrentMap<WorldChunkKey, VeinModel> VEIN_MODELS_BY_ORIGIN_CHUNK = new ConcurrentHashMap<>();
    private static final ConcurrentMap<WorldChunkKey, ConcurrentLinkedQueue<PendingBlockPlacement>> PENDING_ORE_BY_CHUNK = new ConcurrentHashMap<>();
    private static final ConcurrentMap<WorldChunkKey, ConcurrentLinkedQueue<PendingDepositCandidate>> PENDING_DEPOSITS_BY_CHUNK = new ConcurrentHashMap<>();

    public AlgorithmVein(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (ModConfig.COMMON.defaultSpawnEnabled.get()) {
            return false;
        }

        ensureShellClustersInitialized(ctx.random());

        ChunkPos currentChunk = new ChunkPos(ctx.origin());

        ensureModelForSeedChunk(ctx, currentChunk);

        ChunkAccess currentAccess = ctx.level().getChunk(
                currentChunk.x,
                currentChunk.z,
                ChunkStatus.EMPTY,
                false
        );

        if (currentAccess == null || !currentAccess.getPos().equals(currentChunk)) {
            return false;
        }

        return applyPendingOreForGenerationChunk(ctx.level(), currentAccess, currentChunk);
    }

    private static synchronized void ensureShellClustersInitialized(RandomSource random) {
        if (!shellClusterChuncks.isEmpty()) {
            return;
        }

        shellClusterList = new ArrayList<>(PerlinNoiseViewer.createClusters(random));

        for (ShellCluster cluster : shellClusterList) {
            if (WorldBaseGen.veinTypes1.isEmpty()) {
                WorldBaseGen.veinTypes1 = new ArrayList<>(WorldBaseGen.veinTypes);
            }

            int meta = WorldBaseGen.random.nextInt(WorldBaseGen.veinTypes1.size());
            final VeinType veinType = WorldBaseGen.veinTypes1.remove(meta);

            for (Point point : cluster.blacks) {
                Map<Integer, Tuple<Color, Integer>> tupleMap =
                        shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                tupleMap.put(point.y - 256, new Tuple<>(Color.BLACK, veinType.getId()));

                veinCoordination.computeIfAbsent(veinType.getId(), k -> new HashMap<>())
                        .computeIfAbsent(point.x - 256, k -> new LinkedList<>())
                        .add(point.y - 256);
            }

            for (Point point : cluster.grays) {
                Map<Integer, Tuple<Color, Integer>> tupleMap =
                        shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                tupleMap.put(point.y - 256, new Tuple<>(Color.GRAY, veinType.getId()));

                veinCoordination.computeIfAbsent(veinType.getId(), k -> new HashMap<>())
                        .computeIfAbsent(point.x - 256, k -> new LinkedList<>())
                        .add(point.y - 256);
            }

            for (Point point : cluster.lightGrays) {
                Map<Integer, Tuple<Color, Integer>> tupleMap =
                        shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                tupleMap.put(point.y - 256, new Tuple<>(Color.LIGHT_GRAY, veinType.getId()));

                veinCoordination.computeIfAbsent(veinType.getId(), k -> new HashMap<>())
                        .computeIfAbsent(point.x - 256, k -> new LinkedList<>())
                        .add(point.y - 256);
            }
        }

        volcano = PerlinNoiseViewer.createVolcanoClusters(random);
    }

    private static void ensureModelForSeedChunk(
            FeaturePlaceContext<NoneFeatureConfiguration> ctx,
            ChunkPos seedChunk
    ) {
        Tuple<Color, Integer> tuple = getShellTuple(seedChunk);
        if (tuple == null) {
            return;
        }

        VeinType veinType = veinTypeMap.get(tuple.getB());
        if (veinType == null) {
            return;
        }

        WorldChunkKey modelKey = WorldChunkKey.of(ctx.level(), seedChunk);

        VEIN_MODELS_BY_ORIGIN_CHUNK.computeIfAbsent(modelKey, key -> {
            VeinModel model = createVeinModel(ctx.level(), seedChunk, veinType, tuple);
            model.sealAndPushToChunkCache(ctx.level());
            return model;
        });
    }

    @Nullable
    private static Tuple<Color, Integer> getShellTuple(ChunkPos chunkPos) {
        Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.get(chunkPos.x % 256);
        if (tupleMap == null) {
            return null;
        }

        return tupleMap.get(chunkPos.z % 256);
    }

    private static VeinModel createVeinModel(
            WorldGenLevel level,
            ChunkPos seedChunk,
            VeinType veinType,
            Tuple<Color, Integer> tuple
    ) {
        Color color = tuple.getA();
        TypeVein veinSize = resolveVeinSize(color, veinType);

        long seed = makeVeinSeed(level, seedChunk, veinType.getId(), color);
        RandomSource random = RandomSource.create(seed);

        BlockPos startPos = new BlockPos(
                seedChunk.x * 16 + random.nextInt(16),
                2,
                seedChunk.z * 16 + random.nextInt(16)
        );

        VeinModel model = new VeinModel(
                WorldChunkKey.of(level, seedChunk),
                veinType.getId(),
                veinType.getDeposits_meta(),
                seed,
                veinSize
        );


        int surfaceY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE_WG,
                startPos.getX(),
                startPos.getZ()
        );

        Holder<Biome> biome = level.getBiome(new BlockPos(
                startPos.getX(),
                Math.max(level.getMinBuildHeight(), surfaceY - 1),
                startPos.getZ()
        ));

        int chanceType = random.nextInt(101);

        if (chanceType <= 15 && biome.is(BiomeTags.IS_HILL)) {
            simulateHollowSphere(level, model, random, veinType, veinSize, startPos);
        } else if (chanceType <= 80) {
            simulateBranchVein(level, model, random, veinType, veinSize, startPos);
        } else {
            simulateRingVein(level, model, random, veinType, veinSize, startPos);
        }

        return model;
    }

    private static boolean simulateHollowSphere(
            WorldGenLevel level,
            VeinModel model,
            RandomSource random,
            VeinType veinType,
            TypeVein veinSize,
            BlockPos startPos
    ) {
        int radius;
        switch (veinSize) {
            case SMALL -> radius = random.nextInt(4) + 2;
            case MEDIUM -> radius = random.nextInt(6) + 3;
            case BIG -> radius = random.nextInt(7) + 5;
            default -> radius = 4;
        }

        int surfaceY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE_WG,
                startPos.getX(),
                startPos.getZ()
        );

        int centerY = clampToBuildHeight(
                level,
                (int) (surfaceY - radius * 0.9D - random.nextInt(35) - 15)
        );

        BlockPos center = new BlockPos(startPos.getX(), centerY, startPos.getZ());

        double outerSq = radius * radius;
        double midOuterSq = (radius * 0.45D) * (radius * 0.45D);
        double midInnerSq = (radius * 0.35D) * (radius * 0.35D);

        boolean anyOre = false;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + y * y + z * z;
                    BlockPos pos = center.offset(x, y, z);

                    if (distSq <= outerSq && distSq > midOuterSq) {
                        if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                            anyOre = true;
                        }
                    } else if (distSq <= outerSq && distSq >= midInnerSq) {
                        if (veinType.getHeavyOre() != null) {
                            if (random.nextInt(100) > 40) {
                                model.recordOre(
                                        level,
                                        pos,
                                        veinType.getHeavyOre().getStateMeta(veinType.getMeta()),
                                        true
                                );
                                anyOre = true;
                            }
                        } else {
                            if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                                anyOre = true;
                            }
                        }
                    } else if (distSq < midInnerSq) {
                        model.recordOre(level, pos, Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }

        return anyOre;
    }

    private static boolean simulateBranchVein(
            WorldGenLevel level,
            VeinModel model,
            RandomSource random,
            VeinType veinType,
            TypeVein veinSize,
            BlockPos startPos
    ) {
        int surfaceY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE_WG,
                startPos.getX(),
                startPos.getZ()
        );

        BlockPos center = new BlockPos(
                startPos.getX(),
                clampToBuildHeight(level, Math.max(0, surfaceY / 2 + surfaceY / 4 - random.nextInt(20))),
                startPos.getZ()
        );

        int x1 = random.nextInt(veinSize.getMax()) + 3;
        int y1 = random.nextInt(veinSize.getMax()) + 3;
        int z1 = random.nextInt(veinSize.getMax()) + 3;

        int minX = 10;
        int minY = 10;
        int minZ = 10;
        int maxX = -10;
        int maxY = -10;
        int maxZ = -10;

        boolean anyOre = false;

        for (int x = -x1; x <= x1; x++) {
            for (int y = -y1; y < y1; y++) {
                for (int z = -z1; z <= z1; z++) {
                    BlockPos pos = center.offset(x, y, z);

                    int need = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
                    if (need < veinSize.getMinNeed()) {
                        need = 0;
                    }

                    if (random.nextInt(50) > 10) {
                        int denom = 100 - Math.min(need * veinSize.getNeed(), 90);

                        if ((need == 0 || random.nextInt(denom) > 50)
                                && tryRecordRandomOre(level, model, random, veinType, pos, 0)) {
                            minX = Math.min(minX, x);
                            maxX = Math.max(maxX, x);
                            minY = Math.min(minY, y);
                            maxY = Math.max(maxY, y);
                            minZ = Math.min(minZ, z);
                            maxZ = Math.max(maxZ, z);
                            anyOre = true;
                        }
                    } else if (veinType.getHeavyOre() != null) {
                        int denom = 100 - Math.min(need * veinSize.getNeed(), 90);

                        if (random.nextInt(50) > 40 && random.nextInt(denom) > 50) {
                            model.recordOre(
                                    level,
                                    pos,
                                    veinType.getHeavyOre().getStateMeta(veinType.getMeta()),
                                    true
                            );

                            minX = Math.min(minX, x);
                            maxX = Math.max(maxX, x);
                            minY = Math.min(minY, y);
                            maxY = Math.max(maxY, y);
                            minZ = Math.min(minZ, z);
                            maxZ = Math.max(maxZ, z);
                            anyOre = true;
                        }
                    }
                }
            }
        }

        int[] numbers = new int[]{maxY, minY, maxX, minX, maxZ, minZ};
        int[] opposite = new int[]{1, 0, 3, 2, 5, 4};
        int[] yxz = new int[]{0, 0, 0};
        int[][] axisPairs = new int[][]{{1, 2}, {0, 2}, {0, 1}};
        int[][] boundsMap = new int[][]{{3, 2, 5, 4}, {1, 0, 5, 4}, {1, 0, 3, 2}};

        int levelStep = 1;
        int direction = random.nextInt(6);

        while (levelStep < veinSize.getLevel() + 1) {
            int axis = direction / 2;

            if (direction % 2 == 0) {
                for (yxz[axis] = numbers[direction];
                     yxz[axis] < random.nextInt(veinSize.getMax_length()) + 3 + numbers[direction] + 1;
                     yxz[axis]++) {

                    for (yxz[axisPairs[axis][0]] = numbers[boundsMap[axis][0]] + levelStep * 2;
                         yxz[axisPairs[axis][0]] < numbers[boundsMap[axis][1]] - levelStep * 2 + 1;
                         yxz[axisPairs[axis][0]]++) {

                        for (yxz[axisPairs[axis][1]] = numbers[boundsMap[axis][2]] + levelStep * 2;
                             yxz[axisPairs[axis][1]] < numbers[boundsMap[axis][3]] - levelStep * 2 + 1;
                             yxz[axisPairs[axis][1]]++) {

                            BlockPos pos = center.offset(yxz[1], yxz[0], yxz[2]);
                            if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                                anyOre = true;
                            }
                        }
                    }
                }
            } else {
                for (yxz[axis] = numbers[direction];
                     yxz[axis] > numbers[direction] - random.nextInt(veinSize.getMax_length()) - 4;
                     yxz[axis]--) {

                    for (yxz[axisPairs[axis][0]] = numbers[boundsMap[axis][0]] + levelStep * 2;
                         yxz[axisPairs[axis][0]] < numbers[boundsMap[axis][1]] - levelStep * 2 + 1;
                         yxz[axisPairs[axis][0]]++) {

                        for (yxz[axisPairs[axis][1]] = numbers[boundsMap[axis][2]] + levelStep * 2;
                             yxz[axisPairs[axis][1]] < numbers[boundsMap[axis][3]] - levelStep * 2 + 1;
                             yxz[axisPairs[axis][1]]++) {

                            BlockPos pos = center.offset(yxz[1], yxz[0], yxz[2]);
                            if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                                anyOre = true;
                            }
                        }
                    }
                }
            }

            numbers[opposite[direction]] += yxz[axis] - numbers[direction];
            numbers[direction] = yxz[axis];

            levelStep++;

            int prev = direction;
            do {
                direction = random.nextInt(6);
            } while (direction == prev);
        }

        return anyOre;
    }

    private static boolean simulateRingVein(
            WorldGenLevel level,
            VeinModel model,
            RandomSource random,
            VeinType veinType,
            TypeVein veinSize,
            BlockPos startPos
    ) {
        int surfaceY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE_WG,
                startPos.getX(),
                startPos.getZ()
        );

        BlockPos center = new BlockPos(
                startPos.getX(),
                clampToBuildHeight(level, Math.max(0, surfaceY / 2 + surfaceY / 4 - random.nextInt(20))),
                startPos.getZ()
        );

        int R;
        int r;
        int h;

        switch (veinSize) {
            case SMALL -> {
                R = random.nextInt(4) + 3;
                r = random.nextInt(3) + 2;
                h = random.nextInt(3) + 1;
            }
            case MEDIUM -> {
                R = random.nextInt(6) + 3;
                r = random.nextInt(4) + 3;
                h = random.nextInt(3) + 2;
            }
            case BIG -> {
                R = random.nextInt(7) + 5;
                r = random.nextInt(4) + 4;
                h = random.nextInt(5) + 3;
            }
            default -> {
                R = 5;
                r = 3;
                h = 2;
            }
        }

        boolean anyOre = false;
        int outerSq = (R + r) * (R + r);
        int innerSq = r * r;

        for (int y = center.getY() - h; y < center.getY() + h; y++) {
            for (int x = -(R + r); x <= (R + r); x++) {
                for (int z = -(R + r); z <= (R + r); z++) {
                    int distSq = x * x + z * z;
                    BlockPos pos = new BlockPos(center.getX() + x, y, center.getZ() + z);

                    if (distSq <= outerSq && distSq > innerSq) {
                        if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                            anyOre = true;
                        }
                    } else if (distSq <= innerSq) {
                        if (veinType.getHeavyOre() != null) {
                            if (random.nextInt(100) > 40) {
                                model.recordOre(
                                        level,
                                        pos,
                                        veinType.getHeavyOre().getStateMeta(veinType.getMeta()),
                                        true
                                );
                                anyOre = true;
                            }
                        } else {
                            if (tryRecordRandomOre(level, model, random, veinType, pos, 50)) {
                                anyOre = true;
                            }
                        }
                    }
                }
            }
        }

        return anyOre;
    }

    private static boolean tryRecordRandomOre(
            WorldGenLevel level,
            VeinModel model,
            RandomSource random,
            VeinType veinType,
            BlockPos pos,
            int chanceRollMin
    ) {
        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }

        if (veinType.getOres().isEmpty()) {
            return false;
        }

        int meta = random.nextInt(veinType.getOres().size());
        ChanceOre ore = veinType.getOres().get(meta);

        if (!ore.needGenerate(level)) {
            return false;
        }

        if (chanceRollMin > 0 && random.nextInt(100) <= chanceRollMin) {
            return false;
        }

        model.recordOre(level, pos, ore.getBlock(), true);
        model.recordDeepOreCandidate(level, pos, ore.getBlock());
        return true;
    }

    @Nullable
    private static BlockState getDeepOreState(BlockState sourceOreState) {
        Block block = sourceOreState.getBlock();

        for (int i = 0; i < 16; i++) {
            if (block == IUItem.ore.getBlock(i)) {
                return IUItem.deep_ore.getBlockState(i);
            }
        }

        if (block == IUItem.ore2.getBlock(3)) {
            return IUItem.deep_ore1.getBlockState(0);
        }
        if (block == IUItem.ore2.getBlock(4)) {
            return IUItem.deep_ore1.getBlockState(1);
        }
        if (block == IUItem.ore2.getBlock(5)) {
            return IUItem.deep_ore1.getBlockState(2);
        }
        if (block == IUItem.ore2.getBlock(7)) {
            return IUItem.deep_ore1.getBlockState(3);
        }

        for (int i = 0; i < 16; i++) {
            if (block == IUItem.ore3.getBlock(i)) {
                if (i < 12) {
                    return IUItem.deep_ore1.getBlockState(i + 4);
                }
                if (i == 12) {
                    return IUItem.deep_ore2.getBlockState(0);
                }
                if (i == 13) {
                    return IUItem.deep_ore2.getBlockState(1);
                }
                if (i == 15) {
                    return IUItem.deep_ore2.getBlockState(2);
                }
                return null;
            }
        }

        if (block == IUItem.classic_ore.getBlock(1)) {
            return IUItem.deep_ore2.getBlockState(3);
        }
        if (block == IUItem.classic_ore.getBlock(2)) {
            return IUItem.deep_ore2.getBlockState(4);
        }
        if (block == IUItem.classic_ore.getBlock(3)) {
            return IUItem.deep_ore2.getBlockState(5);
        }

        if (block == IUItem.radiationore.getBlock(0)) {
            return IUItem.deep_ore2.getBlockState(6);
        }
        if (block == IUItem.radiationore.getBlock(1)) {
            return IUItem.deep_ore2.getBlockState(7);
        }
        if (block == IUItem.radiationore.getBlock(2)) {
            return IUItem.deep_ore2.getBlockState(8);
        }
        if (block == IUItem.toriyore.getBlock(0)) {
            return IUItem.deep_ore2.getBlockState(9);
        }

        if (block == IUItem.preciousore.getBlock(0)) {
            return IUItem.deep_ore2.getBlockState(10);
        }
        if (block == IUItem.preciousore.getBlock(1)) {
            return IUItem.deep_ore2.getBlockState(11);
        }
        if (block == IUItem.preciousore.getBlock(2)) {
            return IUItem.deep_ore2.getBlockState(12);
        }
        if (block == IUItem.preciousore.getBlock(3)) {
            return IUItem.deep_ore2.getBlockState(13);
        }

        if (block == IUItem.apatite.getBlock(0)) {
            return IUItem.deep_ore2.getBlockState(14);
        }
        if (block == IUItem.apatite.getBlock(1)) {
            return IUItem.deep_ore2.getBlockState(15);
        }
        if (block == IUItem.apatite.getBlock(3)) {
            return IUItem.deep_ore3.getBlockState(0);
        }
        if (block == IUItem.apatite.getBlock(4)) {
            return IUItem.deep_ore3.getBlockState(1);
        }

        return null;
    }

    private static boolean applyPendingOreForGenerationChunk(
            WorldGenLevel level,
            ChunkAccess chunk,
            ChunkPos chunkPos
    ) {
        WorldChunkKey chunkKey = WorldChunkKey.of(level, chunkPos);

        ConcurrentLinkedQueue<PendingBlockPlacement> queue = PENDING_ORE_BY_CHUNK.remove(chunkKey);
        if (queue == null || queue.isEmpty()) {
            return false;
        }

        boolean changed = false;
        Set<WorldChunkKey> touchedModels = new HashSet<>();

        PendingBlockPlacement placement;
        while ((placement = queue.poll()) != null) {
            if (!isPosInChunk(placement.pos, chunkPos)) {
                continue;
            }

            touchedModels.add(placement.modelKey);

            if (level.isOutsideBuildHeight(placement.pos)) {
                continue;
            }

            BlockState current = chunk.getBlockState(placement.pos);

            if (!canApplyOrePlacement(placement, current)) {
                continue;
            }

            BlockState oldState = setBlockStateDirect(chunk, placement.pos, placement.state);
            if (oldState != null && oldState != placement.state) {
                changed = true;
                chunk.setUnsaved(true);
            }
        }

        for (WorldChunkKey modelKey : touchedModels) {
            VeinModel model = VEIN_MODELS_BY_ORIGIN_CHUNK.get(modelKey);
            if (model != null) {
                model.markOreChunkCompleted(chunkKey, null);
            }
        }

        return changed;
    }

    private static boolean applyPendingOreForLoadedChunk(
            ServerLevel level,
            LevelChunk chunk,
            ChunkPos chunkPos
    ) {
        WorldChunkKey chunkKey = WorldChunkKey.of(level, chunkPos.x, chunkPos.z);

        ConcurrentLinkedQueue<PendingBlockPlacement> queue = PENDING_ORE_BY_CHUNK.remove(chunkKey);
        if (queue == null || queue.isEmpty()) {
            return false;
        }

        boolean changed = false;
        Set<WorldChunkKey> touchedModels = new HashSet<>();

        PendingBlockPlacement placement;
        while ((placement = queue.poll()) != null) {
            if (!isPosInChunk(placement.pos, chunkPos)) {
                continue;
            }

            touchedModels.add(placement.modelKey);

            if (level.isOutsideBuildHeight(placement.pos)) {
                continue;
            }

            BlockState current = chunk.getBlockState(placement.pos);

            if (!canApplyOrePlacement(placement, current)) {
                continue;
            }

            BlockState oldState = setBlockStateDirect(chunk, placement.pos, placement.state);
            if (oldState != null && oldState != placement.state) {
                changed = true;
                chunk.setUnsaved(true);

                level.sendBlockUpdated(
                        placement.pos,
                        oldState,
                        placement.state,
                        Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE
                );
            }
        }

        for (WorldChunkKey modelKey : touchedModels) {
            VeinModel model = VEIN_MODELS_BY_ORIGIN_CHUNK.get(modelKey);
            if (model != null) {
                model.markOreChunkCompleted(chunkKey, level);
            }
        }

        return changed;
    }

    private static boolean canApplyOrePlacement(PendingBlockPlacement placement, BlockState current) {
        if (placement.kind == PendingPlacementKind.DEEPSLATE_REPLACE) {
            return canGenerateDeepOreInDeepslate(current);
        }

        if (placement.kind == PendingPlacementKind.STONE_REPLACE) {
            if (placement.state.isAir()) {
                return canGenerateInStone(current);
            }

            return canGenerateInStone(current);
        }

        return false;
    }

    private static boolean applyPendingDepositsForLoadedChunk(
            ServerLevel level,
            LevelChunk chunk,
            ChunkPos chunkPos
    ) {
        WorldChunkKey chunkKey = WorldChunkKey.of(level, chunkPos.x, chunkPos.z);

        ConcurrentLinkedQueue<PendingDepositCandidate> queue = PENDING_DEPOSITS_BY_CHUNK.remove(chunkKey);
        if (queue == null || queue.isEmpty()) {
            return false;
        }

        return applyDepositQueueToLoadedChunk(level, chunk, chunkPos, queue);
    }

    private static boolean applyDepositQueueToLoadedChunk(
            ServerLevel level,
            LevelChunk chunk,
            ChunkPos chunkPos,
            ConcurrentLinkedQueue<PendingDepositCandidate> queue
    ) {
        boolean changed = false;

        PendingDepositCandidate candidate;
        while ((candidate = queue.poll()) != null) {
            VeinModel model = VEIN_MODELS_BY_ORIGIN_CHUNK.get(candidate.modelKey);
            if (model == null || !model.canPlaceMoreDeposits()) {
                continue;
            }

            VeinType veinType = veinTypeMap.get(candidate.veinTypeId);
            if (veinType == null) {
                continue;
            }

            BlockPos placePos = findSurfacePlacementInChunk(chunk, chunkPos, candidate.x, candidate.z);
            if (placePos == null) {
                continue;
            }

            BlockState depositState = buildDepositStateFromChunk(
                    chunk,
                    placePos,
                    veinType,
                    candidate.depositsMeta
            );

            if (depositState == null) {
                continue;
            }

            BlockState support = chunk.getBlockState(placePos.below());
            BlockState target = chunk.getBlockState(placePos);
            BlockState above = chunk.getBlockState(placePos.above());

            if (!canPlaceSurfaceDeposit(support, target, above)) {
                continue;
            }

            if (!model.markDepositPlaced()) {
                continue;
            }

            BlockState oldState = setBlockStateDirect(chunk, placePos, depositState);
            if (oldState != null && oldState != depositState) {
                changed = true;
                chunk.setUnsaved(true);

                level.sendBlockUpdated(
                        placePos,
                        oldState,
                        depositState,
                        Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE
                );
            }
        }

        return changed;
    }

    @Nullable
    private static BlockState setBlockStateDirect(
            ChunkAccess chunk,
            BlockPos pos,
            BlockState state
    ) {
        int y = pos.getY();

        if (y < chunk.getMinBuildHeight() || y >= chunk.getMaxBuildHeight()) {
            return null;
        }

        if (SectionPos.blockToSectionCoord(pos.getX()) != chunk.getPos().x
                || SectionPos.blockToSectionCoord(pos.getZ()) != chunk.getPos().z) {
            return null;
        }

        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
        boolean wasOnlyAir = section.hasOnlyAir();

        if (wasOnlyAir && state.isAir()) {
            return null;
        }

        int localX = pos.getX() & 15;
        int localY = y & 15;
        int localZ = pos.getZ() & 15;


        return section.setBlockState(localX, localY, localZ, state);
    }

    @Nullable
    private static BlockPos findSurfacePlacementInChunk(
            LevelChunk chunk,
            ChunkPos chunkPos,
            int x,
            int z
    ) {
        if (SectionPos.blockToSectionCoord(x) != chunkPos.x
                || SectionPos.blockToSectionCoord(z) != chunkPos.z) {
            return null;
        }

        int localX = x & 15;
        int localZ = z & 15;
        int surfaceY = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, localX, localZ);

        int startY = Math.min(chunk.getMaxBuildHeight() - 2, surfaceY);
        int minY = Math.max(chunk.getMinBuildHeight() + 1, surfaceY - 6);

        for (int y = startY; y >= minY; y--) {
            BlockPos supportPos = new BlockPos(x, y - 1, z);
            BlockPos placePos = new BlockPos(x, y, z);
            BlockPos abovePos = new BlockPos(x, y + 1, z);

            BlockState support = chunk.getBlockState(supportPos);
            BlockState placeState = chunk.getBlockState(placePos);
            BlockState above = chunk.getBlockState(abovePos);

            if (canPlaceSurfaceDeposit(support, placeState, above)) {
                return placePos;
            }
        }

        return null;
    }

    @Nullable
    private static BlockState buildDepositStateFromChunk(
            LevelChunk chunk,
            BlockPos pos,
            VeinType veinType,
            int meta1
    ) {
        FluidState fluidState = chunk.getBlockState(pos).getFluidState();
        boolean waterlogged = !fluidState.isEmpty() && fluidState.getType() == Fluids.WATER;

        if (veinType.getDeposits() == null) {
            if (meta1 < 16) {
                return IUItem.blockdeposits
                        .getBlock(BlockDeposits.Type.getFromID(meta1))
                        .get()
                        .defaultBlockState()
                        .setValue(BlockDeposits.WATERLOGGED, waterlogged);
            } else if (meta1 < 32) {
                return IUItem.blockdeposits1
                        .getBlock(BlockDeposits1.Type.getFromID(meta1 - 16))
                        .get()
                        .defaultBlockState()
                        .setValue(BlockDeposits.WATERLOGGED, waterlogged);
            } else {
                return IUItem.blockdeposits2
                        .getBlock(BlockDeposits2.Type.getFromID(meta1 - 32))
                        .get()
                        .defaultBlockState()
                        .setValue(BlockDeposits.WATERLOGGED, waterlogged);
            }
        }

        BlockState deposits = veinType.getDeposits();

        if (deposits.hasProperty(BlockDeposits.WATERLOGGED)) {
            return deposits.setValue(BlockDeposits.WATERLOGGED, waterlogged);
        }

        return deposits;
    }

    private static boolean canPlaceSurfaceDeposit(BlockState support, BlockState placeState, BlockState above) {
        if (!isValidDepositSupport(support)) {
            return false;
        }

        if (!isReplaceableDepositTarget(placeState)) {
            return false;
        }

        return isOpenAboveForDeposit(above);
    }

    private static boolean isValidDepositSupport(BlockState state) {
        if (state.isAir()) {
            return false;
        }

        if (!state.getFluidState().isEmpty()) {
            return false;
        }

        if (isCoralOrCoralBlock(state) || isWaterPlant(state)) {
            return false;
        }

        return state.is(Tags.Blocks.STONES)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL)
                || state.is(Blocks.MYCELIUM)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.COBBLESTONE);
    }

    private static boolean isReplaceableDepositTarget(BlockState state) {
        if (state.isAir()) {
            return true;
        }

        FluidState fluid = state.getFluidState();
        if (!fluid.isEmpty() && fluid.getType() == Fluids.WATER) {
            return true;
        }

        if (state.is(Blocks.SNOW)) {
            return true;
        }

        return isWaterPlant(state);
    }

    private static boolean isOpenAboveForDeposit(BlockState state) {
        if (state.isAir()) {
            return true;
        }

        FluidState fluid = state.getFluidState();
        if (!fluid.isEmpty() && fluid.getType() == Fluids.WATER) {
            return true;
        }

        return isWaterPlant(state);
    }

    private static boolean canGenerateInStone(BlockState state) {
        FluidState fluidState = state.getFluidState();

        if (state.isAir() || state.liquid()) {
            return false;
        }

        if (!fluidState.isEmpty()) {
            return false;
        }

        return state.is(Tags.Blocks.STONES);
    }

    private static boolean canGenerateDeepOreInDeepslate(BlockState state) {
        FluidState fluidState = state.getFluidState();

        if (state.isAir() || state.liquid()) {
            return false;
        }

        if (!fluidState.isEmpty()) {
            return false;
        }

        return state.is(Blocks.DEEPSLATE) || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static boolean isPosInChunk(BlockPos pos, ChunkPos chunkPos) {
        return SectionPos.blockToSectionCoord(pos.getX()) == chunkPos.x
                && SectionPos.blockToSectionCoord(pos.getZ()) == chunkPos.z;
    }

    private static boolean isCoralOrCoralBlock(BlockState state) {
        return state.is(BlockTags.CORALS) || state.is(BlockTags.CORAL_BLOCKS);
    }

    private static boolean isWaterPlant(BlockState state) {
        Block b = state.getBlock();

        return b instanceof SeagrassBlock
                || b instanceof KelpBlock
                || b == Blocks.SEAGRASS
                || b == Blocks.TALL_SEAGRASS
                || b == Blocks.KELP
                || b == Blocks.KELP_PLANT
                || b == Blocks.SEA_PICKLE;
    }

    private static int clampToBuildHeight(WorldGenLevel level, int y) {
        return Math.max(level.getMinBuildHeight() + 1, Math.min(level.getMaxBuildHeight() - 2, y));
    }

    private static TypeVein resolveVeinSize(Color color, VeinType veinType) {
        if (Color.LIGHT_GRAY.equals(color)) {
            return TypeVein.SMALL;
        }

        if (Color.GRAY.equals(color)) {
            return TypeVein.MEDIUM;
        }

        if (Color.BLACK.equals(color)) {
            return TypeVein.BIG;
        }

        return veinType.getVein();
    }

    private static long makeVeinSeed(WorldGenLevel level, ChunkPos chunkPos, int veinTypeId, Color color) {
        long seed = level.getSeed();

        seed ^= (long) chunkPos.x * 341873128712L;
        seed ^= (long) chunkPos.z * 132897987541L;
        seed ^= (long) veinTypeId * 42317861L;
        seed ^= (long) color.getRGB() * 1181783497276652981L;

        seed ^= seed >>> 33;
        seed *= 0xff51afd7ed558ccdL;
        seed ^= seed >>> 33;
        seed *= 0xc4ceb9fe1a85ec53L;
        seed ^= seed >>> 33;

        return seed;
    }


    private static void clearRuntimeCachesForDimension(String dimension) {
        if (dimension == null) {
            return;
        }

        VEIN_MODELS_BY_ORIGIN_CHUNK.keySet().removeIf(key -> dimension.equals(key.dimension));
        PENDING_ORE_BY_CHUNK.keySet().removeIf(key -> dimension.equals(key.dimension));
        PENDING_DEPOSITS_BY_CHUNK.keySet().removeIf(key -> dimension.equals(key.dimension));
    }

    private static void clearAllRuntimeCaches() {
        VEIN_MODELS_BY_ORIGIN_CHUNK.clear();
        PENDING_ORE_BY_CHUNK.clear();
        PENDING_DEPOSITS_BY_CHUNK.clear();
    }

    private static void drainPendingForLoadedChunks(ServerLevel level, int maxChunks) {
        int processed = 0;
        String dimension = level.dimension().location().toString();

        for (WorldChunkKey key : new ArrayList<>(PENDING_ORE_BY_CHUNK.keySet())) {
            if (processed >= maxChunks) {
                return;
            }

            if (!key.dimension.equals(dimension)) {
                continue;
            }

            LevelChunk chunk = level.getChunkSource().getChunkNow(key.chunkX, key.chunkZ);
            if (chunk == null) {
                continue;
            }

            applyPendingOreForLoadedChunk(level, chunk, chunk.getPos());
            processed++;
        }

        for (WorldChunkKey key : new ArrayList<>(PENDING_DEPOSITS_BY_CHUNK.keySet())) {
            if (processed >= maxChunks) {
                return;
            }

            if (!key.dimension.equals(dimension)) {
                continue;
            }

            LevelChunk chunk = level.getChunkSource().getChunkNow(key.chunkX, key.chunkZ);
            if (chunk == null) {
                continue;
            }

            applyPendingDepositsForLoadedChunk(level, chunk, chunk.getPos());
            processed++;
        }
    }

    public static void setBlockState1(WorldGenLevel level, BlockPos pos, BlockState state, int flags, ChunkAccess access) {
        if (level.isOutsideBuildHeight(pos)) {
            return;
        }

        ChunkPos targetChunk = new ChunkPos(pos);

        if (access != null) {
            if (!access.getPos().equals(targetChunk)) {
                return;
            }

            setBlockStateDirect(access, pos, state);
            access.setUnsaved(true);
            return;
        }


        ChunkAccess chunk = level.getChunk(targetChunk.x, targetChunk.z, ChunkStatus.EMPTY, false);
        if (chunk != null && chunk.getPos().equals(targetChunk)) {
            setBlockStateDirect(chunk, pos, state);
            chunk.setUnsaved(true);
        }
    }

    private enum PendingPlacementKind {
        STONE_REPLACE,
        DEEPSLATE_REPLACE
    }

    private static final class PendingBlockPlacement {

        private final WorldChunkKey modelKey;
        private final BlockPos pos;
        private final BlockState state;
        private final PendingPlacementKind kind;

        private PendingBlockPlacement(
                WorldChunkKey modelKey,
                BlockPos pos,
                BlockState state,
                PendingPlacementKind kind
        ) {
            this.modelKey = modelKey;
            this.pos = pos.immutable();
            this.state = state;
            this.kind = kind;
        }
    }

    private static final class DeepOreCandidate {

        private final BlockPos sourcePos;
        private final BlockState sourceOreState;

        private DeepOreCandidate(BlockPos sourcePos, BlockState sourceOreState) {
            this.sourcePos = sourcePos.immutable();
            this.sourceOreState = sourceOreState;
        }
    }

    private static final class PendingDepositCandidate {

        private final WorldChunkKey modelKey;
        private final int x;
        private final int z;
        private final int veinTypeId;
        private final int depositsMeta;

        private PendingDepositCandidate(
                WorldChunkKey modelKey,
                int x,
                int z,
                int veinTypeId,
                int depositsMeta
        ) {
            this.modelKey = modelKey;
            this.x = x;
            this.z = z;
            this.veinTypeId = veinTypeId;
            this.depositsMeta = depositsMeta;
        }
    }

    private static final class VeinModel {

        private final WorldChunkKey modelKey;
        private final int veinTypeId;
        private final int depositsMeta;
        private final long seed;
        private final TypeVein veinSize;

        private final List<BlockPos> oreSourcePositions = new ArrayList<>();
        private final List<DeepOreCandidate> deepOreCandidates = new ArrayList<>();
        private final Map<BlockPos, PendingBlockPlacement> placements = new LinkedHashMap<>();

        private final Set<WorldChunkKey> requiredOreChunks = ConcurrentHashMap.newKeySet();
        private final Set<WorldChunkKey> completedOreChunks = ConcurrentHashMap.newKeySet();

        private volatile boolean sealed;
        private volatile boolean depositsReleased;
        private volatile int placedDeposits;

        private VeinModel(
                WorldChunkKey modelKey,
                int veinTypeId,
                int depositsMeta,
                long seed,
                TypeVein veinSize
        ) {
            this.modelKey = modelKey;
            this.veinTypeId = veinTypeId;
            this.depositsMeta = depositsMeta;
            this.seed = seed;
            this.veinSize = veinSize;
        }

        private synchronized void recordOre(
                WorldGenLevel level,
                BlockPos pos,
                BlockState state,
                boolean depositSource
        ) {
            if (sealed || level.isOutsideBuildHeight(pos)) {
                return;
            }

            addOrePlacement(level, pos, state);

            if (depositSource) {
                oreSourcePositions.add(pos.immutable());
            }
        }

        private synchronized void recordDeepOreCandidate(
                WorldGenLevel level,
                BlockPos sourcePos,
                BlockState sourceOreState
        ) {
            if (sealed || level.isOutsideBuildHeight(sourcePos)) {
                return;
            }

            if (getDeepOreState(sourceOreState) == null) {
                return;
            }

            deepOreCandidates.add(new DeepOreCandidate(sourcePos, sourceOreState));
        }

        private void addOrePlacement(WorldGenLevel level, BlockPos pos, BlockState state) {
            addOrePlacement(level, pos, state, PendingPlacementKind.STONE_REPLACE);
        }

        private void addOrePlacement(
                WorldGenLevel level,
                BlockPos pos,
                BlockState state,
                PendingPlacementKind kind
        ) {
            WorldChunkKey chunkKey = WorldChunkKey.of(level, pos);

            PendingBlockPlacement placement = new PendingBlockPlacement(
                    modelKey,
                    pos,
                    state,
                    kind
            );

            placements.put(pos.immutable(), placement);
            requiredOreChunks.add(chunkKey);
        }

        private synchronized void appendDeepOrePlacements(WorldGenLevel level) {
            if (deepOreCandidates.isEmpty()) {
                return;
            }

            RandomSource deepRandom = RandomSource.create(seed ^ 0x3C6EF372FE94F82AL);
            Set<BlockPos> reservedPositions = new HashSet<>(placements.keySet());

            for (DeepOreCandidate candidate : deepOreCandidates) {
                BlockState deepOreState = getDeepOreState(candidate.sourceOreState);
                if (deepOreState == null) {
                    continue;
                }

                int maxDeepY = Math.min(candidate.sourcePos.getY() - 8, 0);
                int minDeepY = level.getMinBuildHeight() + 1;
                if (maxDeepY < minDeepY) {
                    continue;
                }

                int deepRange = Math.max(1, maxDeepY - minDeepY + 1);
                for (int attempt = 0; attempt < 24; attempt++) {
                    int y = maxDeepY - deepRandom.nextInt(deepRange);
                    BlockPos deepPos = new BlockPos(candidate.sourcePos.getX(), y, candidate.sourcePos.getZ());
                    if (level.isOutsideBuildHeight(deepPos)) {
                        continue;
                    }

                    BlockPos immutableDeepPos = deepPos.immutable();
                    if (reservedPositions.contains(immutableDeepPos)) {
                        continue;
                    }

                    reservedPositions.add(immutableDeepPos);
                    addOrePlacement(level, immutableDeepPos, deepOreState, PendingPlacementKind.DEEPSLATE_REPLACE);
                    break;
                }
            }

            deepOreCandidates.clear();
        }

        private synchronized void sealAndPushToChunkCache(WorldGenLevel level) {
            if (sealed) {
                return;
            }

            appendDeepOrePlacements(level);
            sealed = true;

            for (PendingBlockPlacement placement : placements.values()) {
                WorldChunkKey targetChunk = WorldChunkKey.of(level, placement.pos);

                PENDING_ORE_BY_CHUNK
                        .computeIfAbsent(targetChunk, k -> new ConcurrentLinkedQueue<>())
                        .add(placement);
            }
        }

        private synchronized void markOreChunkCompleted(
                WorldChunkKey chunkKey,
                @Nullable ServerLevel serverLevel
        ) {
            completedOreChunks.add(chunkKey);

            if (serverLevel != null) {
                tryReleaseDeposits(serverLevel);
            }
        }

        private synchronized boolean isAllOreGenerated() {
            return !requiredOreChunks.isEmpty() && completedOreChunks.containsAll(requiredOreChunks);
        }

        private int getNeedDeposits() {
            return switch (veinSize) {
                case SMALL -> 5;
                case MEDIUM -> 10;
                case BIG -> 15;
            };
        }

        private synchronized boolean canPlaceMoreDeposits() {
            return placedDeposits < getNeedDeposits();
        }

        private synchronized boolean markDepositPlaced() {
            if (placedDeposits >= getNeedDeposits()) {
                return false;
            }

            placedDeposits++;
            return true;
        }

        private synchronized void tryReleaseDeposits(ServerLevel serverLevel) {
            if (depositsReleased) {
                return;
            }

            if (!isAllOreGenerated()) {
                return;
            }

            depositsReleased = true;

            VeinType veinType = veinTypeMap.get(veinTypeId);
            if (veinType == null) {
                return;
            }

            if (oreSourcePositions.isEmpty()) {
                return;
            }

            List<BlockPos> candidates = new ArrayList<>(oreSourcePositions);
            RandomSource random = RandomSource.create(seed ^ 0x6A09E667F3BCC909L);

            while (!candidates.isEmpty()) {
                int index = random.nextInt(candidates.size());
                BlockPos source = candidates.remove(index);

                WorldChunkKey targetChunk = WorldChunkKey.of(
                        serverLevel,
                        SectionPos.blockToSectionCoord(source.getX()),
                        SectionPos.blockToSectionCoord(source.getZ())
                );

                PendingDepositCandidate candidate = new PendingDepositCandidate(
                        modelKey,
                        source.getX(),
                        source.getZ(),
                        veinTypeId,
                        depositsMeta
                );

                PENDING_DEPOSITS_BY_CHUNK
                        .computeIfAbsent(targetChunk, k -> new ConcurrentLinkedQueue<>())
                        .add(candidate);
            }
        }
    }

    private static final class WorldChunkKey {

        private final String dimension;
        private final int chunkX;
        private final int chunkZ;

        private WorldChunkKey(String dimension, int chunkX, int chunkZ) {
            this.dimension = dimension;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        private static WorldChunkKey of(WorldGenLevel level, ChunkPos chunkPos) {
            return new WorldChunkKey(
                    level.getLevel().dimension().location().toString(),
                    chunkPos.x,
                    chunkPos.z
            );
        }

        private static WorldChunkKey of(WorldGenLevel level, BlockPos pos) {
            return of(level, new ChunkPos(pos));
        }

        private static WorldChunkKey of(ServerLevel level, int chunkX, int chunkZ) {
            return new WorldChunkKey(
                    level.dimension().location().toString(),
                    chunkX,
                    chunkZ
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof WorldChunkKey other)) {
                return false;
            }

            return chunkX == other.chunkX
                    && chunkZ == other.chunkZ
                    && dimension.equals(other.dimension);
        }

        @Override
        public int hashCode() {
            int result = dimension.hashCode();
            result = 31 * result + chunkX;
            result = 31 * result + chunkZ;
            return result;
        }
    }

    @EventBusSubscriber(modid = Constants.MOD_ID)
    public static final class VeinWorldEvents {

        private VeinWorldEvents() {
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
                return;
            }

            if (!(event.getChunk() instanceof LevelChunk levelChunk)) {
                return;
            }

            ChunkPos chunkPos = levelChunk.getPos();

            applyPendingOreForLoadedChunk(serverLevel, levelChunk, chunkPos);
            applyPendingDepositsForLoadedChunk(serverLevel, levelChunk, chunkPos);
        }

        @SubscribeEvent
        public static void onLevelTick(LevelTickEvent.Post event) {
            if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
                return;
            }

            if (serverLevel.getServer() == null || !serverLevel.getServer().isRunning()) {
                return;
            }

            drainPendingForLoadedChunks(serverLevel, MAX_PENDING_CHUNKS_PER_TICK);
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                clearRuntimeCachesForDimension(serverLevel.dimension().location().toString());
            }
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            clearAllRuntimeCaches();
        }
    }
}