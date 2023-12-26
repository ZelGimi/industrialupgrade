package com.denfop.world;

import com.denfop.IUItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenOil extends WorldGenerator {

    private final Block spreadBlock;

    public WorldGenOil(Block spreadBlock) {
        this.spreadBlock = spreadBlock;
    }


    @Override
    public boolean generate(@Nonnull final World world, @Nonnull final Random rand, final BlockPos pos) {
        if (WorldGenOres.random.nextInt(10) > 0)
            return false;
        int x = pos.getX();
        int z = pos.getZ();
        int y;
        if (world.isRemote) {
            return false;
        }
        final Chunk chuck = world.getChunkFromBlockCoords(pos);
        int height = chuck.getHeight(pos);
        y = height;
        IBlockState block_state = world.getBlockState(pos);
        while (y > 40 && block_state.getMaterial() == Material.AIR) {
            y--;
            block_state = world.getBlockState(new BlockPos(x, y, z));
        }

        int length = rand.nextInt(20) + 5;
        int weight = rand.nextInt(15) + 5;
        int depth = rand.nextInt(4) + 3;
        int weight1 = 0;
        int weight2 = 0;
        int length1 = 0;
        int length2 = 0;
        if (weight % 3 == 1 || weight % 3 == 2) {
            if (weight % 3 == 2) {
                weight2 = (weight + 1) / 3;
                weight1 = weight2 - 1;
            } else {
                weight2 = (weight - 1) / 3;
                weight1 = weight2 + 1;
            }
        } else {
            weight1 = weight2 = (weight) / 3;
        }

        if (length % 3 == 1 || length % 3 == 2) {
            if (length % 3 == 2) {
                length2 = (length + 1) / 3;
                length1 = length2 - 1;
            } else {
                length2 = (length - 1) / 3;
                length1 = length2 + 1;
            }
        } else {
            length1 = length2 = (length) / 3;
        }
        for (int x1 = -1; x1 > -3; x1--) {
            for (int z1 = 0; z1 < weight; z1++) {
                if (x1 == -1 && (z1 < weight2 || z1 >= weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }else if (x1 == -1 &&  (z1 >= weight2 && z1 < weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, this.spreadBlock.getDefaultState(), 3);
                }else if (x1 == -2 && (z1 >= weight2 && z1 < weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }
            }
        }
        for (int x1 = length; x1 < length + 2; x1++) {
            for (int z1 = 0; z1 < weight; z1++) {
                if (x1 == length && (z1 < weight2 || z1 >= weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }else  if (x1 == length && (z1 >= weight2 && z1 < weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, this.spreadBlock.getDefaultState(), 3);
                }
                else if (x1 == length + 1 && (z1 >= weight2 && z1 < weight1 + weight2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }
            }
        }


        for (int z1 = -1; z1 > -3; z1--) {
            for (int x1 = 0; x1 < length; x1++) {
                if (z1 == -1 && (x1 < length2 || x1 >= length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                } else if (z1 == -1 && (x1 >= length2 && x1 < length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, this.spreadBlock.getDefaultState(), 3);
                }else if (z1 == -2 && (x1 >= length2 && x1 < length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }
            }
        }
        for (int z1 = weight; z1 < weight + 2; z1++) {
            for (int x1 = 0; x1 < length; x1++) {
                if (z1 == weight && (x1 < length2 || x1 >= length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }else if (z1 == weight && (x1 >= length2 && x1 < length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, this.spreadBlock.getDefaultState(), 3);
                } else if (z1 == weight + 1 && (x1 >= length2 && x1 < length1 + length2)) {
                    final BlockPos pos1 = new BlockPos(x + x1, y, z + z1);
                    world.setBlockState(pos1, Blocks.SAND.getDefaultState(), 3);
                }
            }
        }
        for (int y1 = 0; y1 < depth; y1++) {
            for (int x1 = y1 * 2; x1 < length -  y1 * 2; x1++) {
                for (int z1 = y1 * 2; z1 < weight  -  y1 * 2; z1++) {

                    BlockPos stonePos = new BlockPos(x + x1, y - y1, z + z1);
                    world.setBlockState(stonePos, this.spreadBlock.getDefaultState(), 3);

                }
            }
        }
        int geyser = rand.nextInt(2) + 1;
        if (length > weight) {
            int p = length / (geyser + 1);
            for (int x1 = p; geyser != 0; x1 += p, geyser--) {
                height = rand.nextInt(5) + 2;
                for (int y1 = y + 1; y1 < y + 1 + height; y1++) {
                    world.setBlockState(new BlockPos(x + x1, y1, z + weight / 2), this.spreadBlock.getDefaultState(), 3);
                }

            }
        } else {
            int p = weight / (geyser + 1);
            for (int z1 = p; geyser != 0; z1 += p, geyser--) {
                height = rand.nextInt(5 * depth) + depth;
                for (int y1 = y + 1; y1 < y + 1 + height; y1++) {
                    world.setBlockState(new BlockPos(x + length / 2, y1, z + z1), this.spreadBlock.getDefaultState(), 3);
                }

            }
        }
        return true;
    }

}
