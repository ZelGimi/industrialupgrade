package com.denfop.world.vein;

import com.denfop.IUItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgorithmVein {

    static Random random = new Random();

    public static void generate(World world, VeinType veinType, BlockPos pos, Chunk chunk, int meta1) {
        if (random.nextInt(3) > 0) {
            return;
        }
        final int value = random.nextInt(101);
        if (value <= 55) {
            veinType.setVein(TypeVein.SMALL);
        } else if (value < 86) {
            veinType.setVein(TypeVein.MEDIUM);
        } else if (value < 100) {
            veinType.setVein(TypeVein.BIG);
        }
        List<BlockPos> blockPosList = new ArrayList<>();
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
                    if (chunk1 == null || chunkPos == null) {
                        chunk1 = world.getChunkFromBlockCoords(pos1);
                        chunkPos = chunk1.getPos();
                    } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                        chunk1 = world.getChunkFromBlockCoords(pos1);
                        chunkPos = chunk1.getPos();
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
                        blockPosList.add(pos1);
                        world.setBlockState(pos1,
                                ore.getBlock(), 2
                        );
                    } else {

                        if (veinType.getHeavyOre() != null) {
                            if (random.nextInt(50) > 40 && random.nextInt(100 - Math.min(need * veinType
                                    .getVein()
                                    .getNeed(), 90)) > 50 && canGenerate(world, pos1, chunk1)) {
                                blockPosList.add(pos1);
                                world.setBlockState(pos1,
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
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = world.getChunkFromBlockCoords(pos1);
                                    chunkPos = chunk1.getPos();
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                    chunk1 = world.getChunkFromBlockCoords(pos1);
                                    chunkPos = chunk1.getPos();
                                }
                                if (canGenerate(world, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    world.setBlockState(pos1,
                                            ore.getBlock(), 2
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
                                if (chunk1 == null || chunkPos == null) {
                                    chunk1 = world.getChunkFromBlockCoords(pos1);
                                    chunkPos = chunk1.getPos();
                                } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                                    chunk1 = world.getChunkFromBlockCoords(pos1);
                                    chunkPos = chunk1.getPos();
                                }
                                if (canGenerate(world, pos1, chunk1)) {
                                    blockPosList.add(pos1);
                                    world.setBlockState(pos1,
                                            ore.getBlock(), 2
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
        while (ii < k && !blockPosList.isEmpty()) {
            BlockPos pos1 = blockPosList.get(random.nextInt(blockPosList.size()));
            if (chunk1 == null) {
                chunk1 = world.getChunkFromBlockCoords(pos1);
                chunkPos = chunk1.getPos();
            } else if (pos1.getX() >> 4 != chunkPos.x && pos1.getZ() >> 4 != chunk.z) {
                chunk1 = world.getChunkFromBlockCoords(pos1);
                chunkPos = chunk1.getPos();
            }
            int y = chunk1.getHeight(pos1);
            int height1 = y;
            BlockPos pos2 = null;
            IBlockState oldState;
            boolean need = false;
            while (height1 - 3 < y) {
                pos2 = new BlockPos(pos1.getX(), y - 1, pos1.getZ());
                oldState = world.getBlockState(pos2);
                pos2 = new BlockPos(pos1.getX(), y + 1, pos1.getZ());
                final IBlockState upState = world.getBlockState(pos2);
                pos2 = new BlockPos(pos1.getX(), y, pos1.getZ());
                final IBlockState oldState1 = world.getBlockState(pos2);
                final boolean canSpawn = canSpawn(oldState, oldState1, upState);
                if (canSpawn) {
                    need = true;
                    break;
                }
                y--;
            }
            if (!need) {
                blockPosList.remove(pos1);
                continue;
            } else {
                if (meta1 < 16) {
                    world.setBlockState(pos2, IUItem.blockdeposits.getStateMeta(meta1), 3);
                } else {
                    world.setBlockState(pos2, IUItem.blockdeposits1.getStateMeta(meta1 - 16), 3);
                }

                ii++;
                blockPosList.remove(pos1);
            }
        }


    }

    private static boolean canSpawn(IBlockState underState, IBlockState state, final IBlockState upState) {
        final Block block = underState.getBlock();
        final boolean can =
                block == Blocks.GRASS || block == Blocks.GRAVEL || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.COBBLESTONE || block == Blocks.STONE;
        if (can) {
            if (state.getMaterial() == Material.AIR || state.getBlock() == Blocks.TALLGRASS || state.getBlock() == Blocks.DOUBLE_PLANT || state.getBlock() == Blocks.RED_FLOWER) {
                return true;
            }
            return state.getMaterial().isLiquid() && upState.getMaterial().isLiquid();
        }
        return false;

    }

    private static boolean canGenerate(World world, BlockPos pos, final Chunk chunk) {

        IBlockState state = world.getBlockState(pos);

        if (state.getMaterial() == Material.AIR && pos.getY() > chunk.getHeight(pos)) {
            return false;
        }
        if (pos.getY() >= 60) {
            return false;
        }
        if (state.getMaterial() == Material.GLASS || state.getMaterial() == Material.SAND || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.WOOD || state
                .getMaterial()
                .isLiquid()) {
            return false;
        }
        if (state.getMaterial() != Material.AIR && state.getBlock().blockHardness == -1) {
            return false;
        }
        return true;
    }

}
