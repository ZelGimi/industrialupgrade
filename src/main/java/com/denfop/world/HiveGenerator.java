package com.denfop.world;

import com.denfop.IUItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class HiveGenerator implements IWorldGenerator {
    private final Block hiveBlock;

    public HiveGenerator(Block hiveBlock) {
        this.hiveBlock = hiveBlock;
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0 && WorldBaseGen.random.nextBoolean() && WorldBaseGen.random.nextInt(10) <= 5 ) {
            generateHives(world, rand, chunkX * 16, chunkZ * 16);
        }
    }
    public static boolean canReplace(IBlockState blockState, World world, BlockPos pos) {
        Block block = blockState.getBlock();
        return block.isReplaceable(world, pos) && !blockState.getMaterial().isLiquid();
    }
    public static boolean isTreeBlock(IBlockState blockState, World world, BlockPos pos) {
        Block block = blockState.getBlock();
        return block.isLeaves(blockState, world, pos) || block.isWood(world, pos);
    }
    public BlockPos getPosForHive(World world, int x, int z) {
        BlockPos topPos = world.getHeight(new BlockPos(x, 0, z)).down();
        if (topPos.getY() <= 0) {
            return null;
        } else {
            IBlockState blockState = world.getBlockState(topPos);
            if (!isTreeBlock(blockState, world, topPos)) {
                return null;
            } else {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(topPos);

                do {
                    pos.move(EnumFacing.DOWN);
                    blockState = world.getBlockState(pos);
                } while(isTreeBlock(blockState, world, pos));

                return pos.toImmutable();
            }
        }
    }
    private void generateHives(World world, Random rand, int x, int z) {
        int hivesPerChunk = 1;
        for (int i = 0; i < hivesPerChunk; i++) {
            int posX = x + rand.nextInt(16);
            int posZ = z + rand.nextInt(16);
            BlockPos pos = getPosForHive(world, posX, posZ);
            if (pos != null) {
                Biome biome = world.getBiome(pos);
                if (BiomeDictionary.hasType(biome, Type.COLD)) {
                    placeHive(world, pos, 2);
                }
                else if (BiomeDictionary.hasType(biome, Type.FOREST)) {
                    placeHive(world, pos, 0);
                } else if (BiomeDictionary.hasType(biome, Type.PLAINS)) {
                    placeHive(world, pos, 1);
                }  else if (BiomeDictionary.hasType(biome, Type.SWAMP)) {
                    placeHive(world, pos, 3);
                } else if (BiomeDictionary.hasType(biome, Type.JUNGLE)) {
                    placeHive(world, pos, 4);
                }
            }
        }
    }

    private void placeHive(World world, BlockPos pos, int meta) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            final BlockPos placePos = pos.offset(facing);
            if (world.isAirBlock(placePos)) {
                IUItem.hive.item.placeBlockAt(new ItemStack(IUItem.hive, 1, meta), null, world, pos, EnumFacing.NORTH, 0,
                        0, 0, hiveBlock.getStateFromMeta(meta)
                );
                break;
            }
        }
    }

    private BlockPos findLogInChunk(World world, int x, int z) {
        for (int y = 64; y < world.getHeight(); y++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).getBlock() == Blocks.LOG || world.getBlockState(pos).getBlock() == Blocks.LOG2) {
                return pos;
            }
        }
        return null;
    }
}
