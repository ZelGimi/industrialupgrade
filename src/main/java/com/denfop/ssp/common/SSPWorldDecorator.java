package com.denfop.ssp.common;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class SSPWorldDecorator implements IWorldGenerator {

    private final WorldGenerator iridiumOre, platinumOre;

    public SSPWorldDecorator() {
        iridiumOre = new WorldGenMinable(BlocksRegister.iridiumOre.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.STONE));
        platinumOre = new WorldGenMinable(BlocksRegister.platiumOre.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.STONE));
    }

    public void generate(
            Random random,
            int chunkX,
            int chunkZ,
            World world,
            IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider
    ) {
        if (world.provider.getDimension() != 0) {
            return;
        }
        runGenerator(iridiumOre, world, random, chunkX, chunkZ, 4, 5, 30);
        runGenerator(platinumOre, world, random, chunkX, chunkZ, 4, 5, 30);
    }

    // IBlockState blockToGen, int blockAmount, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z) {
    private void runGenerator(
            WorldGenerator gen,
            World world,
            Random rand,
            int chunkX,
            int chunkZ,
            int chance,
            int minHeight,
            int maxHeight
    ) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256) {
            throw new IllegalArgumentException("Ore generated out of bounds");
        }
        int heightDiff = maxHeight - minHeight + 1;

        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }

}
