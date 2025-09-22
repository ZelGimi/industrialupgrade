package com.denfop.world.vein;

import com.denfop.IUItem;
import com.denfop.api.pollution.Vec2f;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.noise.ShellCluster;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AlgorithmVein {

    static Random random = new Random();
    private static Map<ChunkPos, Chunk> chunkPosChunkMap = new HashMap<>();
    private static HashSet<Vec2f> poses = new HashSet<>();
    public static List<ShellCluster> shellClusterList = new ArrayList<>();
    public static ShellCluster volcano;
    public static Map<Integer, Map<Integer, Tuple<Color, Integer>>> shellClusterChuncks = new HashMap<>();
    public static Map<Integer, Map<Integer, List<Integer>>> veinCoordination = new HashMap<>();

    public static void generate(World world, VeinType veinType, BlockPos pos, Chunk chunk, int meta1,
                                final IChunkProvider chunkProvider,
                                final IChunkGenerator chunkGenerator
    ) {


        Biome biome = chunk.getBiome(pos, world.provider.getBiomeProvider());
        final int value = random.nextInt(101 + ((biome instanceof BiomeHills) ? 20 : 0));
        if (value <= 55) {
            veinType.setVein(TypeVein.SMALL);
        } else if (value < 86) {
            veinType.setVein(TypeVein.MEDIUM);
        } else if (value < 100) {
            veinType.setVein(TypeVein.BIG);
        }
        boolean isRadiation = veinType.isRadiation();
        boolean withRadiation = false;
        if (isRadiation) {
            withRadiation = WorldBaseGen.random.nextInt(4) == 0;
        }
        final int chance_type = random.nextInt(101);
        if (chance_type <= 15 && biome instanceof BiomeHills) {
            final int height = chunk.getHeight(pos);
            int radius = 10;
            if (veinType.getVein() == TypeVein.SMALL) {
                radius = world.rand.nextInt(4) + 2;
            } else if (veinType.getVein() == TypeVein.MEDIUM) {
                radius = world.rand.nextInt(6) + 3;
            } else if (veinType.getVein() == TypeVein.BIG) {
                radius = world.rand.nextInt(7) + 5;
            }
            pos = new BlockPos(pos.getX(), height - radius * 0.9 - random.nextInt(15), pos.getZ());
            ChunkPos chunkPos = null;
            Chunk chunk1 = null;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        if (distance <= radius && distance > radius * 0.45) {
                            int meta = random.nextInt(veinType.getOres().size());
                            ChanceOre ore = veinType.getOres().get(meta);
                            final BlockPos pos1 = pos.add(x, y, z);
                            if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                ChunkPos pos2 = new ChunkPos(pos1);
                                chunk1 = chunkPosChunkMap.get(pos2);
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = chunkProvider.provideChunk(pos2.x,pos2.z);
                                    chunkPos = chunk1.getPos();
                                    chunkPosChunkMap.put(chunkPos, chunk1);
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                    chunk1 = chunkPosChunkMap.get(pos2);

                                }

                                if (canGenerateSphere(world, pos1, chunk1)) {
                                    Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                    poses.add(vec2f);
                                    setBlockState1(world, pos1,
                                            (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
                                    );
                                }
                            }
                        } else if (distance <= radius && distance >= radius * 0.35) {
                            final BlockPos pos1 = pos.add(x, y, z);
                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(100) > 40 && canGenerateSphere(world, pos1, chunk1)) {
                                    Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                    poses.add(vec2f);
                                    setBlockState1(world, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2
                                    );
                                }
                            } else {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                    ChunkPos pos2 = new ChunkPos(pos1);
                                    chunk1 = chunkPosChunkMap.get(pos2);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = world.getChunkFromBlockCoords(pos1);
                                        chunkPos = chunk1.getPos();
                                        chunkPosChunkMap.put(chunkPos, chunk1);
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                        chunk1 = chunkPosChunkMap.get(pos2);

                                    }
                                    if (canGenerateSphere(world, pos1, chunk1)) {
                                        Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                        poses.add(vec2f);
                                        setBlockState1(world, pos1,
                                                ore.getBlock(), 2
                                        );
                                    }
                                }
                            }
                        } else if (distance <= radius && distance < radius * 0.35) {
                            final BlockPos pos1 = pos.add(x, y, z);
                            setBlockState1(world, pos1,
                                    Blocks.AIR.getDefaultState(), 2
                            );
                        }
                    }
                }
            }
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
            List<Vec2f> vec2fs = new ArrayList<>(poses);
            while (ii < k && !vec2fs.isEmpty()) {
                Vec2f pos1 = vec2fs.remove(random.nextInt(vec2fs.size()));

                ChunkPos chunkpos = new ChunkPos(pos1.x >> 4, pos1.y >> 4);

                if (chunk1 == null || chunkPos == null) {
                    chunk1 = chunkProvider.provideChunk(chunkpos.x, chunkpos.z);
                    chunkPos = chunk1.getPos();
                    chunkPosChunkMap.put(chunkPos, chunk1);
                } else if (pos1.x >> 4 != chunkPos.x && pos1.y >> 4 != chunk.z) {
                    chunk1 = chunkPosChunkMap.get(chunkpos);

                }
                int y = chunk1.getHeight(new BlockPos(pos1.x, 70, pos1.y));
                int height1 = y;
                BlockPos pos2 = null;
                IBlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.x, y - 1, pos1.y);
                    oldState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y + 1, pos1.y);
                    final IBlockState upState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y, pos1.y);
                    final IBlockState oldState1 = world.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                if (!need) {
                    continue;
                } else {
                    if (meta1 < 16) {
                        setBlockState1(world, pos2, IUItem.blockdeposits.getStateMeta(meta1), 3);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(world, pos2, IUItem.blockdeposits1.getStateMeta(meta1 - 16), 3);
                        } else {
                            setBlockState1(world, pos2, IUItem.blockdeposits2.getStateMeta(meta1 - 32), 3);
                        }
                    }

                    ii++;
                }
            }
        } else if (chance_type <= 80) {

            final int height = chunk.getHeight(pos);
            pos = new BlockPos(pos.getX(), height / 2 + height / 4, pos.getZ());
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
            Chunk chunk1 = null;
            for (int x = -x1; x < x1 + 1; x++) {
                for (int y = -y1; y < y1; y++) {
                    for (int z = -z1; z < z1 + 1; z++) {
                        int meta = random.nextInt(veinType.getOres().size());
                        ChanceOre ore = veinType.getOres().get(meta);
                        int need = Math.max(Math.max(x, y), z);
                        if (need < veinType.getVein().getMinNeed()) {
                            need = 0;
                        }
                        final BlockPos pos1 = pos.add(x, y, z);
                        ChunkPos pos2 = new ChunkPos(pos1);
                        chunk1 = chunkPosChunkMap.get(pos2);
                        if (chunk1 == null || chunkPos == null) {
                            chunk1 = world.getChunkFromBlockCoords(pos1);
                            chunkPos = chunk1.getPos();
                            chunkPosChunkMap.put(chunkPos, chunk1);
                        } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                            chunk1 = chunkPosChunkMap.get(pos2);

                        }
                        if (random.nextInt(50) > 10 && ore.needGenerate(world) && (need == 0 || random.nextInt(100 - Math.min(
                                need * veinType.getVein().getNeed(),
                                90
                        )) > 50) && canGenerate(world, pos1, chunk1)) {
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
                            Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                            poses.add(vec2f);
                            setBlockState1(world, pos1,
                                    (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
                            );
                        } else {

                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(50) > 40 && random.nextInt(100 - Math.min(need * veinType
                                        .getVein()
                                        .getNeed(), 90)) > 50 && canGenerate(world, pos1, chunk1)) {
                                    Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                    poses.add(vec2f);
                                    setBlockState1(world, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2
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
            int level = 1;
            int i = random.nextInt(6);
            while (level < veinType.getVein().getLevel() + 1) {

                if (i % 2 == 0) {
                    for (yxz[i / 2] = numbers[i]; yxz[i / 2] < random.nextInt(veinType
                            .getVein()
                            .getMax_length()) + 3 + numbers[i] + 1; yxz[i / 2]++) {
                        for (yxz[yxz1[i / 2][0]] = numbers[numbers1[i / 2][0]] + level * 2; yxz[yxz1[i / 2][0]] < numbers[numbers1[i / 2][1]] - level * 2 + 1; yxz[yxz1[i / 2][0]]++) {
                            for (yxz[yxz1[i / 2][1]] = numbers[numbers1[i / 2][2]] + level * 2; yxz[yxz1[i / 2][1]] < numbers[numbers1[i / 2][3]] - level * 2 + 1; yxz[yxz1[i / 2][1]]++) {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);

                                if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                    final BlockPos pos1 = pos.add(yxz[1], yxz[0], yxz[2]);
                                    ChunkPos pos2 = new ChunkPos(pos1);
                                    chunk1 = chunkPosChunkMap.get(pos2);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = chunkProvider.provideChunk(pos2.x,pos2.z);
                                        chunkPos = chunk1.getPos();
                                        chunkPosChunkMap.put(chunkPos, chunk1);
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                        chunk1 = chunkPosChunkMap.get(pos2);

                                    }
                                    if (canGenerate(world, pos1, chunk1)) {
                                        Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                        poses.add(vec2f);
                                        setBlockState1(world, pos1,
                                                (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
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
                        for (yxz[yxz1[i / 2][0]] = numbers[numbers1[i / 2][0]] + level * 2; yxz[yxz1[i / 2][0]] < numbers[numbers1[i / 2][1]] - level * 2 + 1; yxz[yxz1[i / 2][0]]++) {
                            for (yxz[yxz1[i / 2][1]] = numbers[numbers1[i / 2][2]] + level * 2; yxz[yxz1[i / 2][1]] < numbers[numbers1[i / 2][3]] - level * 2 + 1; yxz[yxz1[i / 2][1]]++) {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                    final BlockPos pos1 = pos.add(yxz[1], yxz[0], yxz[2]);
                                    ChunkPos pos2 = new ChunkPos(pos1);
                                    chunk1 = chunkPosChunkMap.get(pos2);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = chunkProvider.provideChunk(pos2.x,pos2.z);
                                        chunkPos = chunk1.getPos();
                                        chunkPosChunkMap.put(chunkPos, chunk1);
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                        chunk1 = chunkPosChunkMap.get(pos2);

                                    }
                                    if (canGenerate(world, pos1, chunk1)) {
                                        Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                        poses.add(vec2f);
                                        setBlockState1(world, pos1,
                                                (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
                                        );
                                    }
                                }
                            }
                        }
                    }
                }

                numbers[number2[i]] += yxz[i / 2] - numbers[i];
                numbers[i] = yxz[i / 2];
                level++;
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
            List<Vec2f> vec2fs = new ArrayList<>(poses);
            while (ii < k && !vec2fs.isEmpty()) {
                Vec2f pos1 = vec2fs.remove(random.nextInt(vec2fs.size()));

                ChunkPos chunkpos = new ChunkPos(pos1.x >> 4, pos1.y >> 4);

                if (chunk1 == null || chunkPos == null) {
                    chunk1 = chunkProvider.provideChunk(chunkpos.x, chunkpos.z);
                    chunkPos = chunk1.getPos();
                    chunkPosChunkMap.put(chunkPos, chunk1);
                } else if (pos1.x >> 4 != chunkPos.x && pos1.y >> 4 != chunk.z) {
                    chunk1 = chunkPosChunkMap.get(chunkpos);

                }
                int y = chunk1.getHeight(new BlockPos(pos1.x, 70, pos1.y));
                int height1 = y;
                BlockPos pos2 = null;
                IBlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.x, y - 1, pos1.y);
                    oldState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y + 1, pos1.y);
                    final IBlockState upState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y, pos1.y);
                    final IBlockState oldState1 = chunk1.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                if (!need) {

                    continue;
                } else {
                    if (meta1 < 16) {
                        setBlockState1(world, pos2, IUItem.blockdeposits.getStateMeta(meta1), 3);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(world, pos2, IUItem.blockdeposits1.getStateMeta(meta1 - 16), 3);
                        } else {
                            setBlockState1(world, pos2, IUItem.blockdeposits2.getStateMeta(meta1 - 32), 3);
                        }
                    }

                    ii++;

                }
            }
        } else {

            final int height = chunk.getHeight(pos);
            pos = new BlockPos(pos.getX(), height / 2 + height / 4, pos.getZ());
            final int centerX = (int) pos.getX();
            final int centerY = (int) pos.getY();
            final int centerZ = (int) pos.getZ();
            int R = 0;
            int r = 0;
            int y1 = 0;
            if (veinType.getVein() == TypeVein.SMALL) {
                R = world.rand.nextInt(4) + 3;
                r = world.rand.nextInt(3) + 2;
                y1 = world.rand.nextInt(3) + 1;
            } else if (veinType.getVein() == TypeVein.MEDIUM) {
                R = world.rand.nextInt(6) + 3;
                r = world.rand.nextInt(4) + 3;
                y1 = world.rand.nextInt(3) + 2;
            } else if (veinType.getVein() == TypeVein.BIG) {
                R = world.rand.nextInt(7) + 5;
                r = world.rand.nextInt(4) + 4;
                y1 = world.rand.nextInt(5) + 3;
            }
            ChunkPos chunkPos = null;
            Chunk chunk1 = null;
            for (int y2 = centerY - y1; y2 < centerY + y1; y2++) {
                for (int x = -(R + r); x <= (R + r); x++) {
                    for (int z = -(R + r); z <= (R + r); z++) {
                        if (x * x + z * z <= (R + r) * (R + r) && (x * x + z * z > r * r)) {
                            BlockPos pos1 = new BlockPos(centerX + x, y2, centerZ + z);
                            int meta = random.nextInt(veinType.getOres().size());
                            ChanceOre ore = veinType.getOres().get(meta);
                            if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                ChunkPos pos2 = new ChunkPos(pos1);
                                chunk1 = chunkPosChunkMap.get(pos2);
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = world.getChunkFromBlockCoords(pos1);
                                    chunkPos = chunk1.getPos();
                                    chunkPosChunkMap.put(chunkPos, chunk1);
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                    chunk1 = chunkPosChunkMap.get(pos2);

                                }
                                if (canGenerate(world, pos1, chunk1)) {
                                    Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                    poses.add(vec2f);
                                    setBlockState1(world, pos1,
                                            (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
                                    );
                                }
                            }

                        } else if (x * x + z * z <= (R + r) * (R + r) && (x * x + z * z >= r * r)) {

                            BlockPos pos1 = new BlockPos(centerX + x, y2, centerZ + z);
                            ChunkPos pos2 = new ChunkPos(pos1);
                            chunk1 = chunkPosChunkMap.get(pos2);
                            if (chunk1 == null || chunkPos == null) {
                                chunk1 = chunkProvider.provideChunk(pos2.x,pos2.z);
                                chunkPos = chunk1.getPos();
                                chunkPosChunkMap.put(chunkPos, chunk1);
                            } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                chunk1 = chunkPosChunkMap.get(pos2);

                            }
                            if (veinType.getHeavyOre() != null) {
                                if (random.nextInt(100) > 40 && canGenerate(world, pos1, chunk1)) {
                                    Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                    poses.add(vec2f);
                                    setBlockState1(world, pos1,
                                            veinType.getHeavyOre().getStateMeta(veinType.getMeta()), 2
                                    );
                                }
                            } else {
                                int meta = random.nextInt(veinType.getOres().size());
                                ChanceOre ore = veinType.getOres().get(meta);
                                if (ore.needGenerate(world) && (random.nextInt(100) > 50)) {
                                    ChunkPos pos3 = new ChunkPos(pos1);
                                    chunk1 = chunkPosChunkMap.get(pos3);
                                    if (chunk1 == null || chunkPos == null) {
                                        chunk1 = chunkProvider.provideChunk(pos3.x,pos3.z);
                                        chunkPos = chunk1.getPos();
                                        chunkPosChunkMap.put(chunkPos, chunk1);
                                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                        chunk1 = chunkPosChunkMap.get(pos3);

                                    }
                                    if (canGenerate(world, pos1, chunk1)) {
                                        Vec2f vec2f = new Vec2f(pos1.getX(), pos1.getZ());
                                        poses.add(vec2f);
                                        setBlockState1(world, pos1,
                                                (withRadiation) ? ore.getBlock() : ore.getWithoutRadiation(), 2
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
            List<Vec2f> vec2fs = new ArrayList<>(poses);
            while (ii < k && !vec2fs.isEmpty()) {
                Vec2f pos1 = vec2fs.remove(random.nextInt(vec2fs.size()));

                ChunkPos chunkpos = new ChunkPos(pos1.x >> 4, pos1.y >> 4);

                if (chunk1 == null || chunkPos == null) {
                    chunk1 = chunkProvider.provideChunk(chunkpos.x, chunkpos.z);
                    chunkPos = chunk1.getPos();
                    chunkPosChunkMap.put(chunkPos, chunk1);
                } else if (pos1.x >> 4 != chunkPos.x && pos1.y >> 4 != chunk.z) {
                    chunk1 = chunkPosChunkMap.get(chunkpos);

                }
                int y = chunk1.getHeight(new BlockPos(pos1.x, 70, pos1.y));
                int height1 = y;
                BlockPos pos2 = null;
                IBlockState oldState;
                boolean need = false;
                while (height1 - 3 < y) {
                    pos2 = new BlockPos(pos1.x, y - 1, pos1.y);
                    oldState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y + 1, pos1.y);
                    final IBlockState upState = chunk1.getBlockState(pos2);
                    pos2 = new BlockPos(pos1.x, y, pos1.y);
                    final IBlockState oldState1 = chunk1.getBlockState(pos2);
                    final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                    if (canSpawn) {
                        need = true;
                        break;
                    }
                    y--;
                }
                if (!need) {
                    continue;
                } else {
                    if (meta1 < 16) {
                        setBlockState1(world, pos2, IUItem.blockdeposits.getStateMeta(meta1), 3);
                    } else {
                        if (meta1 < 32) {
                            setBlockState1(world, pos2, IUItem.blockdeposits1.getStateMeta(meta1 - 16), 3);
                        } else {
                            setBlockState1(world, pos2, IUItem.blockdeposits2.getStateMeta(meta1 - 32), 3);
                        }
                    }

                    ii++;
                }
            }
        }
        chunkPosChunkMap.clear();
        poses.clear();
    }

    private static boolean canSpawn(IBlockState underState, IBlockState state, final IBlockState upState) {
        final Block block = underState.getBlock();
        final boolean can =
                block == Blocks.GRASS || block == Blocks.GRAVEL || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.COBBLESTONE || block == Blocks.STONE;
        if (can) {
            if (state.getMaterial() == Material.AIR|| block == Blocks.SNOW_LAYER || state.getBlock() == Blocks.TALLGRASS || state.getBlock() == Blocks.DOUBLE_PLANT || state.getBlock() == Blocks.RED_FLOWER) {
                return true;
            }
            return state.getMaterial().isLiquid() && upState.getMaterial().isLiquid();
        }
        return false;

    }

    public static IBlockState setBlockState(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        return chunk.setBlockState(pos,state);
    }

    private static boolean canGenerate(World world, BlockPos pos, final Chunk chunk) {

        IBlockState state = world.getBlockState(pos);

        if (state.getMaterial() == Material.AIR || pos.getY() >= chunk.getHeight(pos) - 4) {
            return false;
        }
        if (pos.getY() >= 60) {
            return false;
        }
        if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.SAND || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.WOOD || state
                .getMaterial()
                .isLiquid()) {
            return false;
        }
        return state.getMaterial() == Material.AIR || state.getBlock().blockHardness != -1;
    }

    public static boolean setBlockState1(World world, BlockPos pos, IBlockState newState, int flags) {
        if (world.isOutsideBuildHeight(pos)) {
            return false;
        } else if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            ChunkPos chunkPos = new ChunkPos(pos);
            Chunk chunk = chunkPosChunkMap.get(chunkPos);
            if (chunk == null) {
                chunk = world.getChunkFromBlockCoords(pos);
                chunkPosChunkMap.put(chunkPos, chunk);
            }


            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;

            IBlockState iblockstate = setBlockState(world, chunk, pos, newState);

            return true;

        }
    }

    private static boolean canGenerateSphere(World world, BlockPos pos, final Chunk chunk) {

        IBlockState state = world.getBlockState(pos);


        if (pos.getY() >= 60) {
            if (state.getMaterial() == Material.AIR || pos.getY() >= chunk.getHeight(pos) - 4) {
                return false;
            }
            return state.getMaterial() != Material.SAND && state.getMaterial() != Material.CLAY && state.getMaterial() != Material.WOOD && !state
                    .getMaterial()
                    .isLiquid();
        }
        if (state.getMaterial() == Material.AIR || pos.getY() >= chunk.getHeight(pos) - 4) {
            return false;
        }
        if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.SAND || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.WOOD || state
                .getMaterial()
                .isLiquid()) {
            return false;
        }
        return state.getBlock().blockHardness != -1;
    }

}
