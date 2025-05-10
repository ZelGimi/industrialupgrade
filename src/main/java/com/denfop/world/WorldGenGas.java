package com.denfop.world;

import com.denfop.blocks.FluidName;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenGas extends Feature<NoneFeatureConfiguration> {


    public static Map<ChunkPos, GenData> gasMap = new HashMap<>();
    public static Map<TypeGas, FluidName> gasFluidMap = new HashMap<>();
    private BlockState block;
    private TypeGas typeGas;

    public WorldGenGas(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public static void registerFluid() {
        gasFluidMap.put(TypeGas.GAS, FluidName.fluidgas);
        gasFluidMap.put(TypeGas.BROMIDE, FluidName.fluidbromine);
        gasFluidMap.put(TypeGas.CHLORINE, FluidName.fluidchlorum);
        gasFluidMap.put(TypeGas.FLUORINE, FluidName.fluidfluor);
        gasFluidMap.put(TypeGas.IODINE, FluidName.fluidiodine);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (WorldBaseGen.random.nextInt(900) + 1 > 500) {

            WorldGenLevel level = context.level();
            ChunkPos chunkPos = new ChunkPos(context.origin());

            // Generate a random position within the chunk
            int var3 = chunkPos.getMinBlockX() + WorldBaseGen.random.nextInt(16) + 8;
            int var4 = WorldBaseGen.random.nextInt(WorldBaseGen.random.nextInt(WorldBaseGen.random.nextInt(30) + 20) + 8);
            int var5 = chunkPos.getMinBlockZ() + WorldBaseGen.random.nextInt(16) + 8;

            // Get the biome at the position
            Holder<Biome> biome = level.getBiome(new BlockPos(var3, 0, var5));

            // If the biome is ocean or river, choose a gas type
            if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_RIVER)) {
                int rand = WorldBaseGen.random.nextInt(100);
                this.typeGas = (rand < 50) ? TypeGas.GAS : TypeGas.IODINE;
                block = gasFluidMap.get(typeGas).getInstance().get().defaultFluidState().createLegacyBlock();
                return this.generate(level, WorldBaseGen.random, new BlockPos(var3, var4, var5));

            } else {
                // For other biomes, choose from multiple gas types
                int rand = WorldBaseGen.random.nextInt(100);
                if (rand < 50) {
                    this.typeGas = TypeGas.GAS;
                } else if (rand < 75) {
                    this.typeGas = TypeGas.BROMIDE;
                } else {
                    this.typeGas = TypeGas.CHLORINE;
                }
                block = gasFluidMap.get(typeGas).getInstance().get().defaultFluidState().createLegacyBlock();

                return this.generate(level, WorldBaseGen.random, new BlockPos(var3, var4, var5));
            }
        }
        return false;
    }

    private boolean generate(WorldGenLevel world, Random rand, BlockPos pos) {
        int x = pos.getX() - 8;
        int z = pos.getZ() - 8;
        int y = pos.getY();
        int xmin = Integer.MAX_VALUE;
        int xmax = Integer.MIN_VALUE;
        int zmin = Integer.MAX_VALUE;
        int zmax = Integer.MIN_VALUE;
        int ymin = 255;
        int ymax = 0;

        if (rand.nextInt(500) <= 450) {
            return false;
        }
        if (y > 40) {
            return false;
        }

        if (y <= 4) {
            return false;
        }

        boolean can = false;
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
                        BlockState blockState1 = world.getBlockState(new BlockPos(x + j, y + m, z + k));
                        Material material = blockState1.getMaterial();

                        if (m >= 4 && material.isLiquid()) {
                            return false;
                        }
                        if (m < 4 && !material.isSolid() && blockState1 != this.block) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int j = 0; j < 16; j++) {
            for (k = 0; k < 16; k++) {
                for (m = 0; m < 8; m++) {
                    if (arrayOfBoolean[(j * 16 + k) * 8 + m]) {
                        BlockPos pos1 = new BlockPos(x + j, y + m, z + k);
                        world.setBlock(pos1, m >= 4 ? Blocks.AIR.defaultBlockState() : this.block, 2);
                        can = true;
                        if (xmin > x + j) {
                            xmin = x + j;
                        }
                        if (xmax < x + j) {
                            xmax = x + j;
                        }
                        if (zmin > z + k) {
                            zmin = z + k;
                        }
                        if (zmax < z + k) {
                            zmax = z + k;
                        }
                        if (ymin > y + m) {
                            ymin = y + m;
                        }
                        if (ymax < y + m) {
                            ymax = y + m;
                        }
                    }
                }
            }
        }

        for (int j = 0; j < 16; j++) {
            for (k = 0; k < 16; k++) {
                for (m = 4; m < 8; m++) {
                    BlockState blockState = null;
                    boolean need = arrayOfBoolean[(j * 16 + k) * 8 + m];
                    if (need) {
                        blockState = world.getBlockState(new BlockPos(x + j, y + m - 1, z + k));
                    }
                    if (need
                            && (blockState.getBlock() == Blocks.DIRT || blockState.getBlock() == Blocks.WATER)
                            && world.getBrightness(LightLayer.SKY, new BlockPos(x + j, y + m - 1, z + k)) > 0) {
                        world.setBlock(new BlockPos(x + j, y + m - 1, z + k), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                    }
                }
            }
        }

        if (this.block.getMaterial() == Material.WATER) {
            for (int j = 0; j < 16; j++) {
                for (k = 0; k < 16; k++) {
                    for (m = 0; m < 8; m++) {
                        int i2 = (j * 16 + k) * 8 + m;
                        int i1 = arrayOfBoolean[i2]
                                && (j < 15 && arrayOfBoolean[((j + 1) * 16 + k) * 8 + m]
                                || j > 0 && arrayOfBoolean[((j - 1) * 16 + k) * 8 + m]
                                || k < 15 && arrayOfBoolean[(j * 16 + k + 1) * 8 + m]
                                || k > 0 && arrayOfBoolean[(j * 16 + k - 1) * 8 + m]
                                || m < 7 && arrayOfBoolean[(j * 16 + k) * 8 + m + 1]
                                || m > 0 && arrayOfBoolean[i2 - 1]) ? 1 : 0;

                        if (i1 != 0 && (m < 4 || rand.nextInt(2) != 0)
                                && world.getBlockState(new BlockPos(x + j, y + m, z + k)).getMaterial().isSolid()) {
                            world.setBlock(
                                    new BlockPos(x + j, y + m, z + k),
                                    this.block, 3
                            );
                            if (xmin > x + j) {
                                xmin = x + j;
                            }
                            if (xmax < x + j) {
                                xmax = x + j;
                            }
                            if (zmin > z + k) {
                                zmin = z + k;
                            }
                            if (zmax < z + k) {
                                zmax = z + k;
                            }
                            if (ymin > y + m) {
                                ymin = y + m;
                            }
                            if (ymax < y + m) {
                                ymax = y + m;
                            }
                        }
                    }
                }
            }
        }
        int xCenter = (xmin + xmax) / 2;
        int zCenter = (zmin + zmax) / 2;
        int yCenter = (ymin + ymax) / 2;

        if (can) {
            can = false;
            cycle:
            for (int x1 = -1; x1 < 2; x1++)
                for (int z1 = -1; z1 < 2; z1++)
                    for (int y1 = -1; y1 < 2; y1++) {
                        if (world.getBlockState(new BlockPos(xCenter + x1, yCenter + y1, zCenter + z1)).getMaterial().isLiquid()) {
                            can = true;
                            break cycle;
                        }
                    }
            if (can)
                gasMap.put(
                        new ChunkPos(xCenter >> 4, zCenter >> 4),
                        new GenData(yCenter, xCenter, zCenter, typeGas)
                );
        }
        return can;
    }

}
