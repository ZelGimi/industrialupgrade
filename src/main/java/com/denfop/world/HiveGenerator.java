package com.denfop.world;

import com.denfop.IUItem;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

import java.util.Random;

public class HiveGenerator extends Feature<NoneFeatureConfiguration> {
    public HiveGenerator(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    public static boolean isTreeBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        return blockState.is(BlockTags.LEAVES) || blockState.is(BlockTags.LOGS);
    }

    public static boolean canReplace(BlockState blockState) {
        Block block = blockState.getBlock();
        return blockState.canBeReplaced(Fluids.EMPTY) && !blockState.liquid();
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        if (WorldBaseGen.random.nextBoolean() && WorldBaseGen.random.nextInt(10) <= 5) {
            ChunkPos chunkPos = new ChunkPos(context.origin());
            int chunkX = chunkPos.x;
            int chunkZ = chunkPos.z;
            generateHives(world, WorldBaseGen.random, chunkX * 16, chunkZ * 16);
            return true;
        }
        return false;
    }

    private void placeHive(WorldGenLevel world, BlockPos pos, int meta) {
        for (Direction facing : Direction.values()) {
            final BlockPos placePos = pos.offset(facing.getNormal());
            if (world.getBlockState(placePos).isAir()) {
                BlockState state = IUItem.hive.getItem(meta).getBlock().defaultBlockState();
                world.setBlock(pos, state, 11);
                break;
            }
        }
    }

    private void generateHives(WorldGenLevel world, Random rand, int x, int z) {
        int hivesPerChunk = 1;
        for (int i = 0; i < hivesPerChunk; i++) {
            int posX = x + rand.nextInt(16);
            int posZ = z + rand.nextInt(16);
            BlockPos pos = getPosForHive(world, posX, posZ);
            if (pos != null) {
                Holder<Biome> biome = world.getBiome(pos);
                if (biome.is(Tags.Biomes.IS_COLD)) {
                    placeHive(world, pos, 2);
                } else if (biome.is(BiomeTags.IS_FOREST)) {
                    placeHive(world, pos, 0);
                } else if (biome.is(Tags.Biomes.IS_PLAINS)) {
                    placeHive(world, pos, 1);
                } else if (biome.is(Tags.Biomes.IS_SWAMP)) {
                    placeHive(world, pos, 3);
                } else if (biome.is(BiomeTags.IS_JUNGLE)) {
                    placeHive(world, pos, 4);
                }
            }
        }
    }

    public BlockPos getPosForHive(WorldGenLevel world, int x, int z) {
        BlockPos topPos = new BlockPos(x, world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1, z);

        if (topPos.getY() <= 0) {
            return null;
        } else {
            BlockState blockState = world.getBlockState(topPos);
            if (!isTreeBlock(blockState)) {
                return null;
            } else {
                BlockPos.MutableBlockPos pos = topPos.mutable();

                do {
                    pos.move(Direction.DOWN);
                    blockState = world.getBlockState(pos);
                } while (isTreeBlock(blockState) && pos.getY() > 0);

                return pos.immutable();
            }
        }
    }

}
