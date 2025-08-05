package com.denfop.world;

import com.denfop.blocks.FluidName;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;

public class WorldGenOil extends Feature<NoneFeatureConfiguration> {


    private BlockState spreadBlock;
    final FluidName[] fluids = new FluidName[]{FluidName.fluidneft, FluidName.fluidsweet_medium_oil,
            FluidName.fluidsweet_heavy_oil, FluidName.fluidsour_light_oil, FluidName.fluidsour_medium_oil,
            FluidName.fluidsour_heavy_oil};
    ;
    public WorldGenOil(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }


    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (WorldBaseGen.random.nextInt(100) + 1 <= 90)
            return false;
        FluidName fluidName = fluids[context.random().nextInt(fluids.length)];
        this.spreadBlock = fluidName.getInstance().get().getSource().defaultFluidState().createLegacyBlock();

        BlockPos pos = context.origin();
        int y = context.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, context.origin().getX(), context.origin().getZ());

        Holder<Biome> holder = context.level().getBiome(new BlockPos(context.origin().getX(),y,context.origin().getZ()));
        if (!holder.is(Tags.Biomes.IS_DESERT)) {
            return false;
        }
        RandomSource rand = context.random();
        WorldGenLevel world = context.level();
        int x = pos.getX();
        int z = pos.getZ();
        y =  context.random().nextInt(context.random().nextInt(context.random().nextInt(112) + 8) + 8) + 60;
        BlockState block_state = world.getBlockState(pos);
        while (y > 40 && block_state.isAir()) {
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
                        double d7 = (i2 - d4) / (d1 / 2.0D);
                        double d8 = (i4 - d5) / (d2 / 2.0D);
                        double d9 = (i3 - d6) / (d3 / 2.0D);
                        double d10 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (d10 < 1.0D) {
                            arrayOfBoolean[(i2 * 16 + i3) * 8 + i4] = true;
                        }
                    }
                }
            }
        }

        int k;
        int m;

        for (int j = 0; j < 16; j++) {
            for (k = 0; k < 16; k++) {
                for (m = 0; m < 8; m++) {
                    int i1 = (j * 16 + k) * 8 + m;
                    int n = arrayOfBoolean[i1]
                            && (j < 15 && arrayOfBoolean[((j + 1) * 16 + k) * 8 + m]
                            || j > 0 && arrayOfBoolean[((j - 1) * 16 + k) * 8 + m]
                            || k < 15 && arrayOfBoolean[(j * 16 + k + 1) * 8 + m]
                            || k > 0 && arrayOfBoolean[(j * 16 + k - 1) * 8 + m]
                            || m < 7 && arrayOfBoolean[(j * 16 + k) * 8 + m + 1]
                            || m > 0 && arrayOfBoolean[i1 - 1]) ? 1 : 0;

                    if (n != 0) {
                        final BlockState block_state1 = world.getBlockState(new BlockPos(x + j, y + m, z + k));
                        Material material = block_state1.getMaterial();


                        if (m >= 4 && material.isLiquid()) {
                            return false;
                        }
                        if (m < 4 && !material.isSolid() && block_state1
                                .getBlock() != this.spreadBlock.getBlock()) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 16; j++) {
            for (k = 0; k < 16; k++) {
                for (m = 0; m < 8; m++) {
                    boolean need = arrayOfBoolean[(j * 16 + k) * 8 + m];
                    if (need) {

                        world.setBlock(new BlockPos(x + j, y + m, z + k), m >= 4 ? Blocks.AIR.defaultBlockState() :
                                this.spreadBlock, 2);

                    }
                    if (m >= 4) {
                        BlockState block_states = null;
                        if (need) {
                            block_states = world.getBlockState(new BlockPos(x + j, y + m - 1, z + k));
                        }
                        if (need
                                && (block_states.getBlock() == Blocks.DIRT || block_states
                                .getBlock() == Blocks.WATER)
                                && world.getBrightness(LightLayer.SKY, new BlockPos(x + k, y + m - 1, z + k)) > 0) {
                            world.setBlock(new BlockPos(x + j, y + m - 1, z + k), Blocks.GRASS.defaultBlockState(),2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
