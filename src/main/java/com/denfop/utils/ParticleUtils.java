package com.denfop.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class ParticleUtils {

    public static void spawnRecyclerParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;


        server.sendParticles(new DustParticleOptions(new Vector3f(0.3f, 0.8f, 0.3f), 0.6f),
                x, y + 0.05, z, 3, 0.08, 0.01, 0.08, 0.001);


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0.05, 0.01, 0.05, 0.002);
        }


        if (random.nextFloat() < 0.3f) {
            ParticleOptions drop = random.nextBoolean() ? ParticleTypes.DRIPPING_LAVA : ParticleTypes.DRIPPING_WATER;
            server.sendParticles(drop, x, y - 0.2, z, 1, 0, 0, 0, 0);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y + 0.1, z, 1, 0.01, 0.01, 0.01, 0.001);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.ASH, x, y, z, 1, 0.02, 0.01, 0.02, 0.001);
        }
    }
    public static void spawnFallingLeavesParticles(Level level, BlockPos pos, Block leavesBlock) {
        if (!(level instanceof ServerLevel server)) return;
        RandomSource random = level.getRandom();


            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5);
            double y = pos.getY() ;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5);


            double dx = (random.nextDouble() - 0.5) * 0.02;
            double dy = -0.03 - random.nextDouble() * 0.01;
            double dz = (random.nextDouble() - 0.5) * 0.02;


            BlockState state = leavesBlock.defaultBlockState();
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);

            server.sendParticles(particle, x, y, z, 1, dx, dy , dz, 0);

    }

    public static void spawnCompressorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.1f)
            server.sendParticles(ParticleTypes.CLOUD, x, y, z, 3, 0.1, 0.02, 0.1, 0.003);


        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y + 0.05, z, 2, 0.02, 0.01, 0.02, 0.01);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustColorTransitionOptions(
                            new Vector3f(0.4f, 0.6f, 1.0f),
                            new Vector3f(0.5f, 0.5f, 0.5f),
                            0.8f),
                    x, y + 0.1, z, 2, 0.03, 0.01, 0.03, 0.001);
        }
    }
    public static void showFlames(ServerLevel level, BlockPos pos, Direction facing) {
        if (level.random.nextInt(8) == 0) {
            double x = pos.getX() + ((facing.getStepX() * 1.04 + 1.0) / 2.0);
            double y = pos.getY() + level.random.nextFloat() * 0.375;
            double z = pos.getZ() + ((facing.getStepZ() * 1.04 + 1.0) / 2.0);

            if (facing.getAxis() == Direction.Axis.X) {
                z += level.random.nextFloat() * 0.625 - 0.3125;
            } else {
                x += level.random.nextFloat() * 0.625 - 0.3125;
            }

            level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
            level.sendParticles(ParticleTypes.FLAME, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public static void spawnGearParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        Vector3f metalColor = new Vector3f(0.6f, 0.6f, 0.6f);
        if (random.nextFloat() < 0.7f) {
            server.sendParticles(new DustParticleOptions(metalColor, 0.6f),
                    x, y + 0.05, z, 2, 0.02, 0.01, 0.02, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.1, z, 2, 0.02, 0.01, 0.02, 0.01);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y + 0.15, z, 1, 0.01, 0.01, 0.01, 0.002);
        }

        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y + 0.05, z, 1, 0.03, 0.01, 0.03, 0.002);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.02, 0.0, 0.02, 0.005);
        }
    }

    public static void spawnRollingMillParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.FLAME, x, y, z, 2, 0.02, 0.01, 0.02, 0.002);


        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.LAVA, x, y + 0.05, z, 1, 0.01, 0, 0.01, 0.002);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.02, z, 1, 0.02, 0.01, 0.02, 0.002);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 2, 0.04, 0.01, 0.04, 0.002);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.ASH, x, y + 0.05, z, 1, 0.01, 0.01, 0.01, 0.001);
        }
    }
    public static void spawnScrapCollectorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.ASH, x, y, z, 2, 0.04, 0.01, 0.04, 0.001);
        if (random.nextFloat() < 0.4f)
            server.sendParticles(ParticleTypes.SMOKE, x, y + 0.1, z, 1, 0.02, 0.01, 0.02, 0.001);


        if (random.nextFloat() < 0.3f)
            server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y + 0.05, z, 1, 0.01, 0, 0.01, 0.001);


        if (random.nextFloat() < 0.5f) {
            Vector3f color = random.nextBoolean()
                    ? new Vector3f(0.25f, 0.2f, 0.15f)
                    : new Vector3f(0.2f, 0.4f, 0.2f);
            server.sendParticles(new DustParticleOptions(color, 0.9f),
                    x, y, z, 2, 0.02, 0.01, 0.02, 0.001);
        }


        if (random.nextFloat() < 0.1f)
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y + 0.1, z, 1, 0.01, 0.01, 0.01, 0.002);
    }
    public static void spawnFarmParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        if (random.nextFloat() < 0.4f)
            server.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 2, 0.05, 0.01, 0.05, 0.001);


        server.sendParticles(ParticleTypes.SPLASH, x, y - 0.1, z, 1, 0.02, 0.01, 0.02, 0.001);
        if (random.nextFloat() < 0.3f)
            server.sendParticles(ParticleTypes.DRIPPING_WATER, x, y - 0.2, z, 1, 0, 0, 0, 0);


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.4f, 0.7f, 0.2f), 0.9f),
                    x, y, z, 2, 0.02, 0.01, 0.02, 0.001);
        }
    }

    public static void spawnCentrifugeParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.FLAME, x, y, z, 2, 0.03, 0.01, 0.03, 0.002);
        if (random.nextFloat() < 0.3f)
            server.sendParticles(ParticleTypes.LAVA, x, y + 0.1, z, 1, 0.01, 0, 0.01, 0.002);


        if (random.nextFloat() < 0.3f)
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0.02, 0.01, 0.02, 0.002);


        server.sendParticles(new DustParticleOptions(new Vector3f(1.0f, 0.3f, 0.3f), 1.0f),
                x, y + 0.05, z, 2, 0.03, 0.01, 0.03, 0.002);
    }

    public static void spawnOreWashingParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.SPLASH, x, y, z, 2, 0.04, 0.01, 0.04, 0.001);
        if (random.nextFloat() < 0.3f)
            server.sendParticles(ParticleTypes.DRIPPING_WATER, x, y - 0.2, z, 1, 0, 0, 0, 0);


        if (random.nextFloat() < 0.4f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.3f, 0.25f, 0.2f), 0.9f),
                    x, y, z, 2, 0.02, 0.01, 0.02, 0.001);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y + 0.2, z, 1, 0.03, 0.01, 0.03, 0.001);
        }
    }

    public static void spawnExtruderParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;


       if (random.nextFloat() < 0.4f)
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.02, 0.01, 0.02, 0.001);


        ParticleOptions sticky = random.nextBoolean() ? ParticleTypes.DRIPPING_LAVA : ParticleTypes.DRIPPING_HONEY;
        server.sendParticles(sticky, x, y - 0.2, z, 1, 0, 0, 0, 0);


        server.sendParticles(new DustParticleOptions(new Vector3f(1.0f, 0.7f, 0.1f), 0.8f),
                x, y, z, 2, 0.01, 0.01, 0.01, 0.001);
    }

    public static void spawnExtractorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 0.9f, 0.3f), 0.7f),
                x, y, z, 3, 0.04, 0.01, 0.04, 0.002);


        if (random.nextFloat() < 0.4f) {
            ParticleOptions drip = random.nextBoolean() ? ParticleTypes.DRIPPING_HONEY : ParticleTypes.DRIPPING_WATER;
            server.sendParticles(drip, x, y - 0.2, z, 1, 0, 0, 0, 0);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 0.15, z, 1, 0.01, 0.01, 0.01, 0.001);
        }
    }

    public static void spawnCutterParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 2, 0.05, 0.01, 0.05, 0.01);


        if (random.nextFloat() < 0.5f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.4f, 0.4f, 0.4f), 0.9f),
                    x, y + 0.1, z, 2, 0.02, 0.01, 0.02, 0.002);
        }


        if (random.nextFloat() < 0.25f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.03, 0.01, 0.03, 0.001);
        }
    }

    public static void spawnMaceratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.ASH, x, y, z, 4, 0.1, 0.01, 0.1, 0.002);

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.05, z, 2, 0.02, 0.01, 0.02, 0.01);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.3f, 0.2f, 0.1f), 0.8f),
                    x, y, z, 2, 0.03, 0.01, 0.03, 0.002);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.05, 0.01, 0.05, 0.001);
        }
    }

    public static void spawnMineralSeparatorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.ASH, x, y, z, 5, 0.1, 0.01, 0.1, 0.002);



        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.9f, 0.6f, 0.2f), 0.6f),
                    x, y + 0.1, z, 2, 0.03, 0.01, 0.03, 0.001);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.05, z, 1, 0.02, 0.01, 0.02, 0.01);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y + 0.1, z, 1, 0.01, 0.01, 0.01, 0.001);
        }
    }

    public static void spawnMicrochipAssemblerParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 4, 0.04, 0.01, 0.04, 0.01);


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y + 0.1, z, 1, 0.01, 0.01, 0.01, 0.001);
        }





        if (random.nextFloat() < 0.2f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 0.6f, 1.0f), 0.5f),
                    x, y + 0.05, z, 2, 0.02, 0.01, 0.02, 0.001);
        }


        if (random.nextFloat() < 0.15f) {
            server.sendParticles(new DustColorTransitionOptions(
                            new Vector3f(0.1f, 0.6f, 1.0f),
                            new Vector3f(0.2f, 0.9f, 1.0f),
                            1.0f),
                    x, y, z, 2, 0.01, 0.01, 0.01, 0.001);
        }
    }

    public static void spawnAlloySmelterParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.FLAME, x, y, z, 2, 0.08, 0.01, 0.08, 0.005);


        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.1, z, 3, 0.05, 0.02, 0.05, 0.02);
        }


        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y + 0.15, z, 1, 0.04, 0.01, 0.04, 0.002);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.DRIPPING_LAVA, x, y - 0.2, z, 1, 0, 0, 0, 0);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(1.0f, 0.4f, 0.0f), 1.0f),
                    x, y + 0.05, z, 2, 0.05, 0.01, 0.05, 0.002);
        }
    }

    public static void spawnDieselGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.2;
        double z = pos.getZ() + 0.5;


        if (random.nextFloat() < 0.8f) {
            server.sendParticles(ParticleTypes.SMOKE,
                    x, y, z,
                    3, 0.15, 0.05, 0.15, 0.01);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.05f, 0.05f, 0.05f), 0.9f),
                    x, y, z,
                    2, 0.02, 0.01, 0.02, 0.005);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.DRIPPING_LAVA,
                    x + random.nextDouble() * 0.3 - 0.15, y - 0.3, z + random.nextDouble() * 0.3 - 0.15,
                    1, 0, 0, 0, 0);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.CRIT,
                    x, y + 0.1, z,
                    1, 0.02, 0.01, 0.02, 0.01);
        }


        if (random.nextFloat() < 0.15f) {
            server.sendParticles(ParticleTypes.ASH,
                    x, y + 0.05, z,
                    1, 0.05, 0.02, 0.05, 0.001);
            server.sendParticles(ParticleTypes.SMALL_FLAME,
                    x, y + 0.05, z,
                    1, 0.01, 0.01, 0.01, 0.005);
        }
    }

    public static void spawnGasolineGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;


        server.sendParticles(ParticleTypes.CLOUD,
                x, y, z,
                2, 0.05, 0.05, 0.05, 0.01);


        server.sendParticles(new DustParticleOptions(new Vector3f(1.0f, 0.6f, 0.2f), 0.5f),
                x, y + 0.1, z,
                2, 0.05, 0.02, 0.05, 0.01);


        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.CRIT,
                    x, y + 0.15, z,
                    1, 0.02, 0.02, 0.02, 0.01);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.FLAME,
                    x, y + 0.05, z,
                    1, 0.01, 0.01, 0.01, 0.01);
        }
    }

    public static void spawnFusionCoreParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 0.6f, 1.0f), 1.0f),
                x, y, z,
                6, 0.2, 0.2, 0.2, 0.02);


        server.sendParticles(ParticleTypes.PORTAL,
                x, y, z,
                5, 0.3, 0.3, 0.3, 0.1);


        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    x, y + 0.2, z,
                    2, 0.1, 0.1, 0.1, 0.05);
        }


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.END_ROD,
                    x, y, z,
                    1, 0.2, 0.2, 0.2, 0.01);
        }


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustColorTransitionOptions(
                            new Vector3f(0.0f, 0.8f, 1.0f),
                            new Vector3f(0.1f, 0.3f, 0.8f),
                            1.0f),
                    x, y + 0.1, z,
                    4, 0.2, 0.1, 0.2, 0.01);
        }
    }

    public static void spawnRadiationParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        server.sendParticles(new DustParticleOptions(new Vector3f(0.1f, 0.8f, 0.1f), 1.0f),
                x, y, z,
                4, 0.2, 0.2, 0.2, 0.01);


        server.sendParticles(ParticleTypes.PORTAL,
                x, y + 0.3, z,
                2, 0.2, 0.2, 0.2, 0.1);


        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    x, y, z,
                    1, 0.05, 0.05, 0.05, 0.01);
        }


        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.CRIT,
                    x, y + 0.2, z,
                    1, 0.1, 0.1, 0.1, 0.02);
        }


        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.END_ROD,
                    x, y + 0.5, z,
                    1, 0.05, 0.1, 0.05, 0.01);
        }
    }
    public static void spawnLavaGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.8f) {
            server.sendParticles(ParticleTypes.FLAME, x, y, z, 2, 0.1, 0.0, 0.1, 0.001);
            server.sendParticles(ParticleTypes.LAVA, x, y, z, 1, 0.03, 0.0, 0.03, 0.01);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y + 0.1, z, 1, 0.05, 0.01, 0.05, 0.002);
            server.sendParticles(ParticleTypes.ASH, x, y + 0.2, z, 1, 0.02, 0.01, 0.02, 0.002);
        }
    }

    public static void spawnStoneGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.7f) {
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,Blocks.STONE.defaultBlockState()), x, y, z, 2, 0.05, 0.0, 0.05, 0.1);
        }



        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.6f, 0.6f, 0.6f), 0.5f),
                    x, y, z, 1, 0.02, 0.01, 0.02, 0.001);
        }
    }
    public static void spawnFermenterParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX()  + random.nextDouble();
        double y = pos.getY() + 0.5 +  random.nextDouble() / 2;
        double z = pos.getZ()   + random.nextDouble();

        if (random.nextFloat() < 0.7f) {
            server.sendParticles(ParticleTypes.BUBBLE, x, y, z, 2, 0.05, 0.0, 0.05, 0.01);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.DRIPPING_HONEY, x, y - 0.1, z, 1, 0, 0, 0, 0.001);
        }



        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.8f, 0.9f, 0.1f), 0.7f),
                    x, y, z, 2, 0.02, 0.01, 0.02, 0.001);
        }
    }
    public static void spawnApiaryParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX()  + random.nextDouble();
        double y = pos.getY() + 0.5 +  random.nextDouble() / 2;
        double z = pos.getZ()   + random.nextDouble();

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 2, 0.04, 0.01, 0.04, 0.001);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.DRIPPING_HONEY, x, y - 0.1, z, 1, 0, 0, 0, 0.001);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(1.0f, 0.85f, 0.1f), 0.8f),
                    x, y + 0.05, z, 2, 0.01, 0.01, 0.01, 0.001);
        }


    }

    public static void spawnWitherFabricatorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.SOUL, x, y, z, 2, 0.05, 0.0, 0.05, 0.002);
            server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0.03, 0.01, 0.03, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y + 0.1, z, 1, 0.05, 0.05, 0.05, 0.0);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 0.0f, 0.2f), 0.7f),
                    x, y, z, 1, 0.02, 0.01, 0.02, 0.002);
        }
    }
    public static void spawnMagneticCatcherParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 2, 0.1, 0.0, 0.1, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.CRIT, x, y + 0.1, z, 1, 0.03, 0.01, 0.03, 0.01);
            server.sendParticles(new DustParticleOptions(new Vector3f(0.3f, 0.6f, 1.0f), 0.7f),
                    x, y + 0.2, z, 1, 0.01, 0.01, 0.01, 0.001);
        }
    }
    public static void spawnMagneticGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y, z, 2, 0.03, 0.01, 0.03, 0.01);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 0.9f, 0.9f), 0.5f),
                    x, y, z, 1, 0.02, 0.01, 0.02, 0.001);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.ENCHANT, x, y + 0.15, z, 1, 0.05, 0.01, 0.05, 0.001);
        }
    }
    public static void spawnFishingMachineParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.2;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.7f) {
            server.sendParticles(ParticleTypes.BUBBLE, x, y, z, 2, 0.1, 0.0, 0.1, 0.01);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.03, 0.0, 0.03, 0.02);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.FISHING, x, y + 0.2, z, 1, 0.01, 0.01, 0.01, 0.001);
        }
    }
    public static void spawnHydrogenGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 3, 0.05, 0.02, 0.05, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.01, 0.01, 0.01, 0.0005);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.6f, 0.9f, 1.0f), 1.2f),
                    x, y + 0.1, z, 2, 0.03, 0.01, 0.03, 0.0008);
        }
    }
    public static void spawnObsidianGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;


        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 2, 0.03, 0.01, 0.03, 0.0005);
        }
        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y + 0.1, z, 2, 0.05, 0.01, 0.05, 0.0008);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.DRIPPING_LAVA, x, y - 0.1, z, 1, 0, 0, 0, 0.001);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y + 0.2, z, 1, 0.04, 0.01, 0.04, 0.001);
        }
    }
    public static void spawnHeliumGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.BUBBLE_POP, x, y, z, 2, 0.01, 0.01, 0.01, 0.0005);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.7f, 0.9f, 1.0f), 0.8f),
                    x, y, z, 3, 0.02, 0.01, 0.02, 0.0004);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, 0.01, 0.01, 0.01, 0.0005);
        }

        if (random.nextFloat() < 0.1f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y + 0.1, z, 1, 0.03, 0.01, 0.03, 0.0003);
        }
    }
    public static void spawnPlasticParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.DRIPPING_HONEY, x, y - 0.1, z, 1, 0, 0, 0, 0.001);
        }

        if (random.nextFloat() < 0.5f) {
            server.sendParticles(ParticleTypes.SMOKE, x, y + 0.1, z, 2, 0.04, 0.01, 0.04, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            Vector3f color = switch (random.nextInt(3)) {
                case 0 -> new Vector3f(1.0f, 0.9f, 0.5f);
                case 1 -> new Vector3f(0.9f, 0.8f, 0.3f);
                default -> new Vector3f(0.7f, 1.0f, 0.7f);
            };
            server.sendParticles(new DustParticleOptions(color, 0.7f),
                    x, y, z, 2, 0.01, 0.01, 0.01, 0.001);
        }
    }
    public static void spawnElectrolyzerParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.1;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 2, 0.03, 0.01, 0.03, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.BUBBLE_POP, x, y - 0.05, z, 1, 0.01, 0.01, 0.01, 0.0005);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(new DustParticleOptions(new Vector3f(0.2f, 1.0f, 0.9f), 0.8f),
                    x, y + 0.05, z, 2, 0.02, 0.01, 0.02, 0.0006);
        }

        if (random.nextFloat() < 0.2f) {
            server.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, 0.02, 0.01, 0.02, 0.0008);
        }
    }

    public static void spawnWaterGeneratorParticles(Level level, BlockPos pos, RandomSource random) {
        if (!(level instanceof ServerLevel server)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;

        if (random.nextFloat() < 0.6f) {
            server.sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 2, 0.01, 0.0, 0.01, 0.001);
        }

        if (random.nextFloat() < 0.4f) {
            server.sendParticles(ParticleTypes.BUBBLE, x, y - 0.1, z, 2, 0.02, 0.01, 0.02, 0.005);
        }

        if (random.nextFloat() < 0.3f) {
            server.sendParticles(ParticleTypes.CLOUD, x, y + 0.05, z, 1, 0.03, 0.01, 0.03, 0.002);
        }
    }

}
