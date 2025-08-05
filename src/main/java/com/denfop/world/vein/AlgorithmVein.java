package com.denfop.world.vein;

import com.denfop.IUItem;
import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.BlockDeposits2;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.noise.PerlinNoiseViewer;
import com.denfop.world.vein.noise.Point;
import com.denfop.world.vein.noise.ShellCluster;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import java.awt.*;
import java.util.List;
import java.util.*;

import static com.denfop.world.vein.VeinType.veinTypeMap;

public class AlgorithmVein extends Feature<NoneFeatureConfiguration> {
    public static List<VeinStructure> veinStructureList = new LinkedList<>();
    public static ShellCluster volcano;
    public static Map<Integer, Map<Integer, Tuple<Color, Integer>>> shellClusterChuncks = new HashMap<>();
    static Random random = new Random();
    private static Map<ChunkPos, ChunkAccess> chunkPosChunkMap = new HashMap<>();
    private static List<ShellCluster> shellClusterList = new ArrayList<>();

    public AlgorithmVein(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    private static boolean generate(WorldGenLevel level, VeinType veinType, BlockPos blockPos, ChunkAccess chunk, int meta1, Color color) {


        int height2 = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());

        Holder<Biome> biome = level.getBiome(new BlockPos(blockPos.getX(), height2, blockPos.getZ()));
        if (color == Color.LIGHT_GRAY) {
            veinType.setVein(TypeVein.SMALL);
        } else if (color == Color.GRAY) {
            veinType.setVein(TypeVein.MEDIUM);
        } else if (color == Color.BLACK) {
            veinType.setVein(TypeVein.BIG);
        }
        final int chance_type = random.nextInt(101);
        BlockPos pos;
        if (chance_type <= 15 && biome.is(BiomeTags.IS_HILL)) {
            List<BlockPos> blockPosList = new ArrayList<>();
            final int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
            int radius = 10;
            if (veinType.getVein() == TypeVein.SMALL) {
                radius = level.getRandom().nextInt(4) + 2;
            } else if (veinType.getVein() == TypeVein.MEDIUM) {
                radius = level.getRandom().nextInt(6) + 3;
            } else if (veinType.getVein() == TypeVein.BIG) {
                radius = level.getRandom().nextInt(7) + 5;
            }
            pos = new BlockPos(blockPos.getX(), (int) (height - radius * 0.9 - random.nextInt(35)), blockPos.getZ());
            ChunkPos chunkPos = null;
            ChunkAccess chunk1 = null;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        if (distance <= radius && distance > radius * 0.45) {
                            int meta = random.nextInt(veinType.getOres().size());
                            ChanceOre ore = veinType.getOres().get(meta);
                            final BlockPos pos1 = pos.offset(x, y, z);
                            if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                    if (chunk1 == null) {
                                        continue;
                                    }
                                    chunkPos = chunk1.getPos();
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                    if (chunk1 == null) {
                                        continue;
                                    }
                                    chunkPos = chunk1.getPos();
                                }
                                if (canGenerateSphere(level, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    setBlockState1(level, pos1,
                                            ore.getBlock(), 2, chunk1
                                    );
                                }
                            }
                        } else if (distance <= radius && distance >= radius * 0.35) {
                            final BlockPos pos1 = pos.offset(x, y, z);
                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(100) > 40 && canGenerateSphere(level, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    setBlockState1(level, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2, chunk1
                                    );
                                }
                            } else {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    }
                                    if (canGenerateSphere(level, pos1, chunk1)) {
                                        blockPosList.add(pos1);
                                        setBlockState1(level, pos1,
                                                ore.getBlock(), 2, chunk1
                                        );
                                    }
                                }
                            }
                        } else if (distance <= radius && distance < radius * 0.35) {
                            final BlockPos pos1 = pos.offset(x, y, z);
                            setBlockState1(level, pos1,
                                    Blocks.AIR.defaultBlockState(), 2, chunk1
                            );
                        }
                    }
                }
            }
            int ii = 0;
            int k = switch (veinType.getVein()) {
                case SMALL -> 5;
                case MEDIUM -> 10;
                case BIG -> 15;
            };
            while (ii < k && !blockPosList.isEmpty()) {
                BlockPos pos1 = blockPosList.get(random.nextInt(blockPosList.size()));
                if (chunk1 == null) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    chunkPos = chunk1.getPos();
                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    chunkPos = chunk1.getPos();
                }
                int y = chunk1.getHeight(Heightmap.Types.WORLD_SURFACE, pos1.getX(), pos1.getZ());
                int height1 = y;
                BlockPos pos2 = null;
                BlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.getX(), y - 1, pos1.getZ());
                    oldState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y + 1, pos1.getZ());
                    final BlockState upState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y, pos1.getZ());
                    final BlockState oldState1 = level.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                blockPosList.remove(pos1);
                if (need) {
                    FluidState fluidState = level.getFluidState(pos2);
                    if (meta1 < 16) {
                        setBlockState1(level, pos2, IUItem.blockdeposits.getBlock(BlockDeposits.Type.getFromID(meta1)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(level, pos2, IUItem.blockdeposits1.getBlock(BlockDeposits1.Type.getFromID(meta1 - 16)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        } else {
                            setBlockState1(level, pos2, IUItem.blockdeposits2.getBlock(BlockDeposits2.Type.getFromID(meta1 - 32)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        }
                    }

                    ii++;
                }
            }
        } else if (chance_type <= 80) {

            List<BlockPos> blockPosList = new LinkedList<>();
            final int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
            pos = new BlockPos(blockPos.getX(), height / 2 + height / 4, blockPos.getZ());
            int x1 = random.nextInt(veinType.getVein().getMax()) + 3;
            int y1 = random.nextInt(veinType.getVein().getMax()) + 3;
            int z1 = random.nextInt(veinType.getVein().getMax()) + 3;
            int minX = 10;
            int minY = 10;
            int minZ = 10;
            int maxX = -10;
            int maxY = -10;
            int maxZ = -10;
            ChunkPos chunkPos = null;
            ChunkAccess chunk1 = null;
            for (int x = -x1; x < x1 + 1; x++) {
                for (int y = -y1; y < y1; y++) {
                    for (int z = -z1; z < z1 + 1; z++) {
                        int meta = random.nextInt(veinType.getOres().size());
                        ChanceOre ore = veinType.getOres().get(meta);
                        int need = Math.max(Math.max(x, y), z);
                        if (need < veinType.getVein().getMinNeed()) {
                            need = 0;
                        }
                        final BlockPos pos1 = pos.offset(x, y, z);
                        if (chunk1 == null || chunkPos == null) {
                            chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                            if (chunk1 == null) {
                                continue;
                            }
                            chunkPos = chunk1.getPos();
                        } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                            chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                            if (chunk1 == null) {
                                continue;
                            }
                            chunkPos = chunk1.getPos();
                        }
                        if (random.nextInt(50) > 10 && ore.needGenerate(level) && (need == 0 || random.nextInt(100 - Math.min(
                                need * veinType.getVein().getNeed(),
                                90
                        )) > 50) && canGenerate(level, pos1, chunk1)) {
                            if (x < minX) {
                                minX = x;
                            }
                            if (x > maxX) {
                                maxX = x;
                            }
                            if (y < minY) {
                                minY = y;
                            }
                            if (y > maxY) {
                                maxY = y;
                            }
                            if (z < minZ) {
                                minZ = z;
                            }
                            if (z > maxZ) {
                                maxZ = z;
                            }
                            blockPosList.add(pos1);
                            setBlockState1(level, pos1,
                                    ore.getBlock(), 2, chunk1
                            );
                        } else {

                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(50) > 40 && random.nextInt(100 - Math.min(need * veinType
                                        .getVein()
                                        .getNeed(), 90)) > 50 && canGenerate(level, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    setBlockState1(level, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2, chunk1
                                    );
                                    if (x < minX) {
                                        minX = x;
                                    }
                                    if (x > maxX) {
                                        maxX = x;
                                    }
                                    if (y < minY) {
                                        minY = y;
                                    }
                                    if (y > maxY) {
                                        maxY = y;
                                    }
                                    if (z < minZ) {
                                        minZ = z;
                                    }
                                    if (z > maxZ) {
                                        maxZ = z;
                                    }
                                }
                            }

                        }
                    }
                }
            }
            int[] numbers = new int[]{maxY, minY, maxX, minX, maxZ, minZ};
            int[] number2 = new int[]{1, 0, 3, 2, 5, 4};
            int[] yxz = new int[]{0, 0, 0};
            int[][] yxz1 = new int[][]{{1, 2}, {0, 2}, {0, 1}};
            int[][] numbers1 = new int[][]{{3, 2, 5, 4}, {1, 0, 5, 4}, {1, 0, 3, 2}};
            chunk1 = null;
            chunkPos = null;
            int level1 = 1;
            int i = random.nextInt(6);
            while (level1 < veinType.getVein().getLevel() + 1) {

                if (i % 2 == 0) {
                    for (yxz[i / 2] = numbers[i]; yxz[i / 2] < random.nextInt(veinType
                            .getVein()
                            .getMax_length()) + 3 + numbers[i] + 1; yxz[i / 2]++) {
                        for (yxz[yxz1[i / 2][0]] = numbers[numbers1[i / 2][0]] + level1 * 2; yxz[yxz1[i / 2][0]] < numbers[numbers1[i / 2][1]] - level1 * 2 + 1; yxz[yxz1[i / 2][0]]++) {
                            for (yxz[yxz1[i / 2][1]] = numbers[numbers1[i / 2][2]] + level1 * 2; yxz[yxz1[i / 2][1]] < numbers[numbers1[i / 2][3]] - level1 * 2 + 1; yxz[yxz1[i / 2][1]]++) {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);

                                if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                    final BlockPos pos1 = pos.offset(yxz[1], yxz[0], yxz[2]);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    }
                                    if (canGenerate(level, pos1, chunk1)) {
                                        blockPosList.add(pos1);
                                        setBlockState1(level, pos1,
                                                ore.getBlock(), 2, chunk1
                                        );
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (yxz[i / 2] = numbers[i]; yxz[i / 2] > numbers[i] - random.nextInt(veinType
                            .getVein()
                            .getMax_length()) - 4; yxz[i / 2]--) {
                        for (yxz[yxz1[i / 2][0]] = numbers[numbers1[i / 2][0]] + level1 * 2; yxz[yxz1[i / 2][0]] < numbers[numbers1[i / 2][1]] - level1 * 2 + 1; yxz[yxz1[i / 2][0]]++) {
                            for (yxz[yxz1[i / 2][1]] = numbers[numbers1[i / 2][2]] + level1 * 2; yxz[yxz1[i / 2][1]] < numbers[numbers1[i / 2][3]] - level1 * 2 + 1; yxz[yxz1[i / 2][1]]++) {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                    final BlockPos pos1 = pos.offset(yxz[1], yxz[0], yxz[2]);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null) {
                                            continue;
                                        }
                                        chunkPos = chunk1.getPos();
                                    }
                                    if (canGenerate(level, pos1, chunk1)) {
                                        blockPosList.add(pos1);
                                        setBlockState1(level, pos1,
                                                ore.getBlock(), 2, chunk1
                                        );
                                    }
                                }
                            }
                        }
                    }
                }

                numbers[number2[i]] += yxz[i / 2] - numbers[i];
                numbers[i] = yxz[i / 2];
                level1++;
                int prev = i;
                random.nextInt();
                i = random.nextInt(6);
                while (i != prev) {
                    i = random.nextInt(6);
                }
            }
            int k = 0;
            switch (veinType.getVein()) {
                case SMALL:
                    k = 5;
                    break;
                case MEDIUM:
                    k = 10;
                    break;
                case BIG:
                    k = 15;
                    break;
            }
            chunk1 = null;
            int ii = 0;
            blockPosList = new ArrayList<>(blockPosList);
            while (ii < k && !blockPosList.isEmpty()) {
                BlockPos pos1 = blockPosList.get(random.nextInt(blockPosList.size()));
                if (chunk1 == null) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    if (chunk1 == null) {
                        i++;
                        continue;
                    }
                    chunkPos = chunk1.getPos();
                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    if (chunk1 == null) {
                        i++;
                        continue;
                    }
                    chunkPos = chunk1.getPos();
                }
                int y = chunk1.getHeight(Heightmap.Types.WORLD_SURFACE, pos1.getX(), pos1.getZ());
                ;
                int height1 = y;
                BlockPos pos2 = null;
                BlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.getX(), y - 1, pos1.getZ());
                    oldState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y + 1, pos1.getZ());
                    final BlockState upState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y, pos1.getZ());
                    final BlockState oldState1 = level.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                blockPosList.remove(pos1);
                if (need) {
                    FluidState fluidState = level.getFluidState(pos2);
                    if (meta1 < 16) {
                        setBlockState1(level, pos2, IUItem.blockdeposits.getBlock(BlockDeposits.Type.getFromID(meta1)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(level, pos2, IUItem.blockdeposits1.getBlock(BlockDeposits1.Type.getFromID(meta1 - 16)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        } else {
                            setBlockState1(level, pos2, IUItem.blockdeposits2.getBlock(BlockDeposits2.Type.getFromID(meta1 - 32)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        }
                    }

                    ii++;
                }
            }
        } else {


            final int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
            ;
            pos = new BlockPos(blockPos.getX(), height / 2 + height / 4, blockPos.getZ());
            final int centerX = (int) pos.getX();
            final int centerY = (int) pos.getY();
            final int centerZ = (int) pos.getZ();
            int R = 0;
            int r = 0;
            int y1 = 0;
            if (veinType.getVein() == TypeVein.SMALL) {
                R = level.getRandom().nextInt(4) + 3;
                r = level.getRandom().nextInt(3) + 2;
                y1 = level.getRandom().nextInt(3) + 1;
            } else if (veinType.getVein() == TypeVein.MEDIUM) {
                R = level.getRandom().nextInt(6) + 3;
                r = level.getRandom().nextInt(4) + 3;
                y1 = level.getRandom().nextInt(3) + 2;
            } else if (veinType.getVein() == TypeVein.BIG) {
                R = level.getRandom().nextInt(7) + 5;
                r = level.getRandom().nextInt(4) + 4;
                y1 = level.getRandom().nextInt(5) + 3;
            }
            List<BlockPos> blockPosList = new LinkedList<>();
            ChunkPos chunkPos = null;
            ChunkAccess chunk1 = null;
            for (int y2 = centerY - y1; y2 < centerY + y1; y2++) {
                for (int x = -(R + r); x <= (R + r); x++) {
                    for (int z = -(R + r); z <= (R + r); z++) {
                        if (x * x + z * z <= (R + r) * (R + r) && (x * x + z * z > r * r)) {
                            BlockPos pos1 = new BlockPos(centerX + x, y2, centerZ + z);
                            int meta = random.nextInt(veinType.getOres().size());
                            ChanceOre ore = veinType.getOres().get(meta);
                            if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                    if (chunk1 == null)
                                        continue;
                                    chunkPos = chunk1.getPos();
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                    if (chunk1 == null)
                                        continue;
                                    chunkPos = chunk1.getPos();
                                }
                                if (canGenerate(level, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    setBlockState1(level, pos1,
                                            ore.getBlock(), 2, chunk1
                                    );
                                }
                            }

                        } else if (x * x + z * z <= (R + r) * (R + r) && (x * x + z * z >= r * r)) {

                            BlockPos pos1 = new BlockPos(centerX + x, y2, centerZ + z);
                            if (chunk1 == null || chunkPos == null) {
                                chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                if (chunk1 == null)
                                    continue;
                                chunkPos = chunk1.getPos();
                            } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                if (chunk1 == null)
                                    continue;
                                chunkPos = chunk1.getPos();
                            }
                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(100) > 40 && canGenerate(level, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    setBlockState1(level, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2, chunk1
                                    );
                                }
                            } else {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(level) && (random.nextInt(100) > 50)) {
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null)
                                            continue;
                                        chunkPos = chunk1.getPos();
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                                        chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                                        if (chunk1 == null)
                                            continue;
                                        chunkPos = chunk1.getPos();
                                    }
                                    if (canGenerate(level, pos1, chunk1)) {
                                        blockPosList.add(pos1);
                                        setBlockState1(level, pos1,
                                                ore.getBlock(), 2, chunk1
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
            chunk1 = null;
            int ii = 0;
            int k = 0;
            switch (veinType.getVein()) {
                case SMALL:
                    k = 5;
                    break;
                case MEDIUM:
                    k = 10;
                    break;
                case BIG:
                    k = 15;
                    break;
            }
            blockPosList = new ArrayList<>(blockPosList);
            while (ii < k && !blockPosList.isEmpty()) {
                BlockPos pos1 = blockPosList.get(random.nextInt(blockPosList.size()));
                if (chunk1 == null) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    chunkPos = chunk1.getPos();
                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.getPos().z) {
                    chunk1 = level.getChunk(pos1.getX() >> 4, pos1.getZ() >> 4, ChunkStatus.EMPTY, false);
                    chunkPos = chunk1.getPos();
                }
                int y = chunk1.getHeight(Heightmap.Types.WORLD_SURFACE, pos1.getX(), pos1.getZ());
                ;
                int height1 = y;
                BlockPos pos2 = null;
                BlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.getX(), y - 1, pos1.getZ());
                    oldState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y + 1, pos1.getZ());
                    final BlockState upState = level.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.getX(), y, pos1.getZ());
                    final BlockState oldState1 = level.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                blockPosList.remove(pos1);
                if (need) {
                    FluidState fluidState = level.getFluidState(pos2);
                    if (meta1 < 16) {
                        setBlockState1(level, pos2, IUItem.blockdeposits.getBlock(BlockDeposits.Type.getFromID(meta1)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(level, pos2, IUItem.blockdeposits1.getBlock(BlockDeposits1.Type.getFromID(meta1 - 16)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        } else {
                            setBlockState1(level, pos2, IUItem.blockdeposits2.getBlock(BlockDeposits2.Type.getFromID(meta1 - 32)).get().defaultBlockState().setValue(BlockDeposits.WATERLOGGED, fluidState != Fluids.EMPTY.defaultFluidState() && fluidState.getType() == Fluids.WATER), 3, chunk1);
                        }
                    }

                    ii++;
                }
            }
        }
        chunkPosChunkMap.clear();
        return true;
    }

    public static void setBlockState1(WorldGenLevel level, BlockPos pPos, BlockState pState, int p_46607_, ChunkAccess access) {
        if (access != null)
            access.setBlockState(pPos, pState, false);
        else {
            ChunkPos chunkPos = new ChunkPos(pPos);
            level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, false).setBlockState(pPos, pState, false);
        }

    }

    private static boolean canGenerateSphere(WorldGenLevel world, BlockPos pos, final ChunkAccess chunk) {

        BlockState state = chunk.getBlockState(pos);
        FluidState fluidState = chunk.getFluidState(pos);
        if (state.isAir() || state.liquid())
            return false;
        if (!fluidState.isEmpty())
            return false;
        if (pos.getY() >= world.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 4) {
            return state.is(Tags.Blocks.STONES);
        }
        return state.is(Tags.Blocks.STONES);
    }

    private static boolean canSpawn(BlockState underState, BlockState state, final BlockState upState) {
        final Block block = underState.getBlock();
        final boolean can =
                block == Blocks.GRASS_BLOCK || block == Blocks.GRAVEL || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.COBBLESTONE || block == Blocks.STONE;

        if (can) {
            if (isCoralOrCoralBlock(underState) || isWaterPlant(underState)) {
                return false;
            }

            return state.isAir() || state.getMapColor(null, null) == MapColor.PLANT || state.liquid();
        }
        return false;

    }

    private static boolean canGenerate(WorldGenLevel world, BlockPos pos, final ChunkAccess chunk) {

        BlockState state = chunk.getBlockState(pos);
        FluidState fluidState = chunk.getFluidState(pos);
        if (state.isAir() || state.liquid())
            return false;
        if (!fluidState.isEmpty())
            return false;
        if (pos.getY() >= world.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 4) {
            return state.is(Tags.Blocks.STONES);
        }
        return state.is(Tags.Blocks.STONES);
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

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159749_) {
        if (shellClusterChuncks.isEmpty()) {
            shellClusterList = PerlinNoiseViewer.createClusters(p_159749_.random());
            shellClusterList = new ArrayList<>(shellClusterList);
            for (ShellCluster cluster : shellClusterList) {
                if (WorldBaseGen.veinTypes1.isEmpty()) {
                    WorldBaseGen.veinTypes1 = new ArrayList<>(WorldBaseGen.veinTypes);
                }
                int meta = WorldBaseGen.random.nextInt(WorldBaseGen.veinTypes1.size());
                final VeinType veinType = WorldBaseGen.veinTypes1.remove(meta);
                for (com.denfop.world.vein.noise.Point point : cluster.blacks) {
                    Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                    tupleMap.put(point.y - 256, new Tuple<>(Color.BLACK, veinType.getId()));
                }
                for (com.denfop.world.vein.noise.Point point : cluster.grays) {
                    Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                    tupleMap.put(point.y - 256, new Tuple<>(Color.GRAY, veinType.getId()));
                }
                for (Point point : cluster.lightGrays) {
                    Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.computeIfAbsent(point.x - 256, k -> new HashMap<>());
                    tupleMap.put(point.y - 256, new Tuple<>(Color.LIGHT_GRAY, veinType.getId()));
                }
            }
            volcano = PerlinNoiseViewer.createVolcanoClusters(p_159749_.random());
        }
        BlockPos origin = p_159749_.origin();
        ChunkPos chunkPos = new ChunkPos(origin);
        Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.get(chunkPos.x % 256);
        if (tupleMap == null)
            return false;
        Tuple<Color, Integer> tuple = tupleMap.get(chunkPos.z % 256);

        if (tuple != null) {
            ChunkAccess chunk = p_159749_.level().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, false);
            VeinStructure veinStructure;
            VeinType veinType = veinTypeMap.get(tuple.getB());
            Color color = tuple.getA();
            veinStructure = new VeinStructure(p_159749_.level(), veinType,
                    new BlockPos(chunkPos.x * 16 + random.nextInt(16), 2, chunkPos.z * 16 + random.nextInt(16)), chunk, veinType.getDeposits_meta());

            return AlgorithmVein.generate(p_159749_.level(), veinStructure.getVeinType(), veinStructure.getBlockPos(), veinStructure.getChunk(), veinStructure.getDepositsMeta(), color);
        } else {
            return false;
        }
    }
}
