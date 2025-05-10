package com.denfop.world;

import com.denfop.blocks.FluidName;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

public class WorldGenOil extends Feature<NoneFeatureConfiguration> {


    private BlockState spreadBlock;

    public WorldGenOil(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }


    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        this.spreadBlock = FluidName.fluidneft.getInstance().get().defaultFluidState().createLegacyBlock();

        BlockPos pos = context.origin();
        Holder<Biome> holder = context.level().getBiome(pos);
        if (!holder.is(BiomeTags.HAS_DESERT_PYRAMID)) {
            return false;
        }
        RandomSource rand = context.random();
        WorldGenLevel world = context.level();
        int x = pos.getX();
        int z = pos.getZ();
        int y;
        final ChunkAccess chuck = world.getChunk(pos);
        int height = chuck.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        y = height;
        BlockState block_state = world.getBlockState(pos);
        while (y > 40 && block_state.getMaterial() == Material.AIR) {
            y--;
            block_state = world.getBlockState(new BlockPos(x, y, z));
        }
        if (y <= 40)
            return false;
        y -= 4;
        boolean[] arrayOfBoolean = new boolean[2048];
        int i = rand.nextInt(4) + 4;
        for (int j = 0; j < i; j++) {
            double d1 = rand.nextDouble() * 6.0D + 3.0D;
            double d2 = rand.nextDouble() * 4.0D + 2.0D;
            double d3 = rand.nextDouble() * 6.0D + 3.0D;
            double d4 = rand.nextDouble() * (16.0D - d1 - 2.0D) + 1.0D + d1 / 2.0D;
            double d5 = rand.nextDouble() * (8.0D - d2 - 4.0D) + 2.0D + d2 / 2.0D;
            double d6 = rand.nextDouble() * (16.0D - d3 - 2.0D) + 1.0D + d3 / 2.0D;
            for (int i2 = 1; i2 < 15; i2++) {
                for (int i3 = 1; i3 < 15; i3++) {
                    for (int i4 = 1; i4 < 7; i4++) {
                        double d7 = (i2 - d4) / d1 / 2.0D;
                        double d8 = (i4 - d5) / d2 / 2.0D;
                        double d9 = (i3 - d6) / d3 / 2.0D;
                        double d10 = d7 * d7 + d8 * d8 + d9 * d9;
                        if (d10 < 1.0D)
                            arrayOfBoolean[(i2 * 16 + i3) * 8 + i4] = true;
                    }
                }
            }
        }
        int k;
        for (k = 0; k < 16; k++) {
            for (int m = 0; m < 16; m++) {
                for (int n = 0; n < 8; n++) {
                    int i1 = (k * 16 + m) * 8 + n;
                    int i2 = (arrayOfBoolean[i1] && ((k < 15 && arrayOfBoolean[((k + 1) * 16 + m) * 8 + n]) || (k > 0 && arrayOfBoolean[((k - 1) * 16 + m) * 8 + n]) || (m < 15 && arrayOfBoolean[(k * 16 + m + 1) * 8 + n]) || (m > 0 && arrayOfBoolean[(k * 16 + m - 1) * 8 + n]) || (n < 7 && arrayOfBoolean[(k * 16 + m) * 8 + n + 1]) || (n > 0 && arrayOfBoolean[i1 - 1]))) ? 1 : 0;
                    if (i2 != 0) {
                        BlockState block_state1 = world.getBlockState(new BlockPos(x + k, y + n, z + m));
                        Material localMaterial = block_state1.getMaterial();
                        if (n >= 4 && localMaterial.isLiquid())
                            return false;
                        if (n < 4 && !localMaterial.isLiquid() && block_state1.getBlock() != this.spreadBlock.getBlock())
                            return false;
                    }
                }
            }
        }
        for (k = 0; k < 16; k++) {
            for (int m = 0; m < 16; m++) {
                for (int n = 0; n < 8; n++) {
                    boolean need = arrayOfBoolean[(k * 16 + m) * 8 + n];
                    if (need)
                        world.setBlock(new BlockPos(x + k, y + n, z + m), (n >= 4) ? Blocks.AIR.defaultBlockState() : this.spreadBlock, 2);
                    if (n >= 4) {
                        BlockState block_states = null;
                        if (need)
                            block_states = world.getBlockState(new BlockPos(x + k, y + n - 1, z + m));
                        if (need && (block_states
                                .getBlock() == Blocks.DIRT || block_states
                                .getBlock() == Blocks.WATER) && world.getBrightness(LightLayer.SKY, new BlockPos(x + k, y + m - 1, z + k)) > 0)
                            world.setBlock(new BlockPos(x + k, y + n - 1, z + m), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        return true;
    }
}
