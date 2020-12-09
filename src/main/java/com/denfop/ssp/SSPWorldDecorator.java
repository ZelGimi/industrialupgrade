package com.denfop.ssp;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class SSPWorldDecorator implements IWorldGenerator {
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == 0) {
			runGenerator(com.denfop.ssp.molecular.BlocksRegister.iridiumOre.getDefaultState(), random.nextInt(4) + 1,
					BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
			runGenerator(com.denfop.ssp.molecular.BlocksRegister.platiumOre.getDefaultState(), random.nextInt(4) + 1,
					BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
		}
	}

	private void runGenerator(IBlockState blockToGen, int blockAmount, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z) {
		// if (12 < 0 || 26 > 60 || 12 > 26)
		// 	throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
		WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, blockToReplace);
		int heightdiff = 26 - 12 + 1;
		for (int i = 0; i < 3; i++) {
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = 12 + rand.nextInt(heightdiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
