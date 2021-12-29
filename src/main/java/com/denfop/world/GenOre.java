package com.denfop.world;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

public class GenOre implements IWorldGenerator {


    final Fluid fluid = FluidName.fluidneft.getInstance();
    final Block block = fluid.getBlock();

    public static void init() {
        GameRegistry.registerWorldGenerator(new GenOre(), 0);
    }

    @Override
    public void generate(
            final Random random,
            final int chunkX,
            final int chunkZ,
            final World world,
            final IChunkGenerator chunkGenerator,
            final IChunkProvider chunkProvider
    ) {
        if (world.provider.getDimension() == 0) {
            int var2;
            int var3;
            int var4;
            int var5;

            if (world.provider.getDimension() == 0 && random.nextInt(100) + 1 > 70 && world.getBiome(new BlockPos(chunkX * 16, 0,
                    chunkZ * 16
            )) == Biome.getBiome(2)) {
                for (var2 = 0; var2 < 1; ++var2) {
                    var3 = chunkX * 16 + random.nextInt(16) + 8;
                    var4 = random.nextInt(random.nextInt(random.nextInt(112) + 8) + 8) + 60;
                    var5 = chunkZ * 16 + random.nextInt(16) + 8;
                    if (block != null) {
                        new WorldGenOil(block, block, 3)
                                .generate(world, random, new BlockPos(var3, var4, var5));
                    }

                }
            }
        }
        if (Config.DimensionList.contains(world.provider.getDimension())) {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }

    }

    private void generateSurface(World world, Random random, int x, int y) {

        if (Config.EnableToriyOre) {
            this.addOreSpawn(IUItem.toriyore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(2), 11, 10,
                    70
            );
        }

        if (Config.MikhailOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.AluminiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(1), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.VanadyOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(2), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.TungstenOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(3), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.CobaltOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(4), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.MagnesiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(5), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.NickelOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(6), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.PlatiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(7), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.TitaniumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(8), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.ChromiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(9), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.SpinelOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(10), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.SilverOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(11), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.ZincOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(12), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.ManganeseOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(13), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.IridiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(14), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.GermaniumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(15), world, random, x, y, 16, 16, 3 + random.nextInt(3), 13, 0, 70);
        }
        if (Config.MagnetiteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.CalaveriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(1), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.GalenaOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(2), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.NickeliteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(3), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.PyriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(4), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.QuartziteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(5), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.UraniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(6), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.AzuriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(7), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.RhodoniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(8), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.AlfilditOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(9), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.EuxeniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(10), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }
        if (Config.SmithsoniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(11), world, random, x, y, 16, 16, 3 + random.nextInt(2), 9, 10, 70);
        }

        if (Config.AmericiumOre) {
            this.addOreSpawn(IUItem.radiationore.getStateFromMeta(0), world, random, x, y, 16, 16, 2 + random.nextInt(2), 14, 10,
                    70
            );
        }
        if (Config.NeptuniumOre) {
            this.addOreSpawn(IUItem.radiationore.getStateFromMeta(1), world, random, x, y, 16, 16, 2 + random.nextInt(2), 16, 10,
                    70
            );
        }
        if (Config.CuriumOre) {
            this.addOreSpawn(IUItem.radiationore.getStateFromMeta(2), world, random, x, y, 16, 16, 2 + random.nextInt(2), 10, 10,
                    70
            );
        }

        if (Config.RubyOre) {
            this.addOreSpawn(
                    IUItem.preciousore.getStateFromMeta(0),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    8,
                    10,
                    70
            );
        }
        if (Config.SapphireOre) {
            this.addOreSpawn(
                    IUItem.preciousore.getStateFromMeta(1),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    8,
                    10,
                    70
            );
        }
        if (Config.TopazOre) {
            this.addOreSpawn(
                    IUItem.preciousore.getStateFromMeta(2),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    8,
                    10,
                    70
            );
        }


    }

    public void addOreSpawn(
            IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ,
            int maxVeinSize, int chancesToSpawn, int minY, int maxY
    ) {
        for (int i = 0; i < chancesToSpawn; i++) {
            int posX = blockXPos + random.nextInt(maxX);
            int posY = minY + random.nextInt(maxY - minY);
            int posZ = blockZPos + random.nextInt(maxZ);
            (new WorldGenMinable(block, maxVeinSize)).generate(world, random,
                    new BlockPos(posX,
                            posY, posZ
                    )
            );

        }
    }

}


