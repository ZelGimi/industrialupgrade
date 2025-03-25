package com.denfop.world;

import com.denfop.blocks.FluidName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenGas extends WorldGenerator {

    public static Map<ChunkPos, GenData> gasMap = new HashMap<>();
    public static Map<TypeGas, FluidName> gasFluidMap = new HashMap<>();
    private final TypeGas typeGas;

    private Block block = FluidName.fluidgas.getInstance().getBlock();

    public WorldGenGas(TypeGas typeGas) {
        block = gasFluidMap.get(typeGas).getInstance().getBlock();
        this.typeGas = typeGas;
    }

    public static void registerFluid() {
        gasFluidMap.put(TypeGas.GAS, FluidName.fluidgas);
        gasFluidMap.put(TypeGas.BROMIDE, FluidName.fluidbromine);
        gasFluidMap.put(TypeGas.CHLORINE, FluidName.fluidchlorum);
        gasFluidMap.put(TypeGas.FLUORINE, FluidName.fluidfluor);
        gasFluidMap.put(TypeGas.IODINE, FluidName.fluidiodine);
    }

    @Override
    public boolean generate(@Nonnull final World world, @Nonnull final Random rand, @Nonnull final BlockPos pos) {
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
                        final IBlockState block_state1 = world.getBlockState(new BlockPos(x + j, y + m, z + k));
                        Material localMaterial = block_state1.getMaterial();

                        if (m >= 4 && localMaterial.isLiquid()) {
                            return false;
                        }
                        if (m < 4 && !localMaterial.isSolid() && block_state1
                                .getBlock() != this.block) {
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
                        final BlockPos pos1 = new BlockPos(x + j, y + m, z + k);
                        world.setBlockState(pos1, m >= 4 ? Blocks.AIR.getDefaultState() :
                                this.block.getDefaultState(), 2);
                        if (m < 4) {
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
        }
        for (int j = 0; j < 16; j++) {
            for (k = 0; k < 16; k++) {
                for (m = 4; m < 8; m++) {
                    IBlockState block_states = null;
                    boolean need = arrayOfBoolean[(j * 16 + k) * 8 + m];
                    if (need) {
                        block_states = world.getBlockState(new BlockPos(x + j, y + m - 1, z + k));
                    }
                    if (need
                            && (block_states.getBlock() == Blocks.DIRT || block_states
                            .getBlock() == Blocks.WATER)
                            && world.getSkylightSubtracted() > 0) {
                        world.setBlockState(new BlockPos(x + j, y + m - 1, z + k), Blocks.GRASS.getDefaultState());

                    }

                }
            }
        }

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
                            world.setBlockState(
                                    new BlockPos(x + j, y + m, z + k),
                                    this.block.getDefaultState()
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
                            can = true;
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
            for (int x1 = -1;x1 < 2;x1++)
                for (int z1 = -1;z1 < 2;z1++)
                    for (int y1 = -1;y1 < 2;y1++){
                        if (world.getBlockState(new BlockPos(xCenter+x1,yCenter+y1,zCenter+z1)).getMaterial().isLiquid()){
                            can = true;
                            break cycle;
                        }
                    }
            if (can)
            gasMap.put(
                    new ChunkPos(xCenter >> 4, zCenter >> 4),
                    new GenData(yCenter,xCenter,zCenter, typeGas)
            );
        }
        return true;
    }

}
