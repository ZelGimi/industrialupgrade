package com.denfop.world;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

public class WorldGenOres implements IWorldGenerator {


    final Fluid fluid = FluidName.fluidneft.getInstance();
    final Block block = fluid.getBlock();

    public static void init() {
        GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);

    }

    private static int randomX(Chunk chunk, Random rnd) {
        return chunk.x * 16 + rnd.nextInt(16);
    }

    private static int randomZ(Chunk chunk, Random rnd) {
        return chunk.z * 16 + rnd.nextInt(16);
    }

    private static void genRubberTree(Random rnd, long seed, Chunk chunk, float baseScale) {
        rnd.setSeed(seed);
        Biome[] biomes = new Biome[6];

        int rubberTrees;
        int i;
        for (rubberTrees = 0; rubberTrees < 5; ++rubberTrees) {
            int x = chunk.x * 16 + 8 + (rubberTrees & 2) * 15;
            i = chunk.z * 16 + 8 + ((rubberTrees & 2) >>> 1) * 15;
            BlockPos pos = new BlockPos(x, chunk.getWorld().getSeaLevel(), i);
            biomes[rubberTrees] = chunk.getWorld().getBiomeProvider().getBiome(pos, Biomes.PLAINS);
        }

        rubberTrees = 0;

        for (Biome biome : biomes) {
            if (biome != null) {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    rubberTrees += rnd.nextInt(10) + 5;
                }

                if (  BiomeDictionary.hasType(
                        biome,
                        BiomeDictionary.Type.FOREST
                ) || BiomeDictionary.hasType(
                        biome,
                        BiomeDictionary.Type.JUNGLE
                )) {
                    rubberTrees += rnd.nextInt(5) + 1;
                }
            }
        }

        rubberTrees = Math.round((float) rubberTrees * baseScale);
        rubberTrees /= 2;
        if (rubberTrees > 0 && rnd.nextInt(100) < rubberTrees) {
            WorldGenRubTree gen = new WorldGenRubTree(false);
            for (i = 0; i < rubberTrees; ++i) {
                if (!gen.generate(
                        chunk.getWorld(),
                        rnd,
                        new BlockPos(randomX(chunk, rnd), chunk.getWorld().getSeaLevel(), randomZ(chunk, rnd))
                )) {
                    rubberTrees -= 3;
                }
            }
        }


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

            if (random.nextInt(100) + 1 > 70 && world.getBiome(new BlockPos(chunkX * 16, 0,
                    chunkZ * 16
            )) == Biome.getBiome(2)) {
                for (var2 = 0; var2 < 1; ++var2) {
                    var3 = chunkX * 16 + random.nextInt(16) + 8;
                    var4 = random.nextInt(random.nextInt(random.nextInt(112) + 8) + 8) + 60;
                    var5 = chunkZ * 16 + random.nextInt(16) + 8;
                    if (block != null) {
                        new WorldGenOil(block, block)
                                .generate(world, random, new BlockPos(var3, var4, var5));
                    }

                }
            }
            if (random.nextInt(1000) + 1 > 900) {

                var3 = chunkX * 16 + random.nextInt(16) + 8;
                var4 = random.nextInt(random.nextInt(random.nextInt(112) + 8) + 8);
                var5 = chunkZ * 16 + random.nextInt(16) + 8;
                new WorldGenMinerals().generate(world, random, new BlockPos(var3, var4, var5));


            }
            if (random.nextInt(900) + 1 > 800) {

                var3 = chunkX * 16 + random.nextInt(16) + 8;
                var4 = random.nextInt(random.nextInt(random.nextInt(30) + 20) + 8);
                var5 = chunkZ * 16 + random.nextInt(16) + 8;
                new WorldGenGas().generate(world, random, new BlockPos(var3, var4, var5));


            }
        }
        Chunk chunk = chunkProvider.provideChunk(chunkX, chunkZ);
        generateSurface(world, random, chunkX * 16, chunkZ * 16, chunkGenerator, chunkProvider, chunk);

        genRubberTree(random, random.nextLong(), chunk, 2);

    }

    private void generateSurface(
            World world, Random random, int x, int y,
            final IChunkGenerator chunkGenerator,
            final IChunkProvider chunkProvider,
            final Chunk chunk
    ) {


        if (Config.EnableToriyOre) {
            this.addOreSpawn(
                    IUItem.toriyore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(2), 10, 10,
                    70
            );
        }

        if (Config.MikhailOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.AluminiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(1), world, random, x, y, 16, 16, 3 + random.nextInt(3), 16, 0, 70);
        }
        if (Config.VanadiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(2), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.TungstenOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(3), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.CobaltOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(4), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.MagnesiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(5), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.NickelOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(6), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.PlatiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(7), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.TitaniumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(8), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.ChromiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(9), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.SpinelOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(10), world, random, x, y, 16, 16, 3 + random.nextInt(3), 16, 0, 70);
        }
        if (Config.SilverOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(11), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.ZincOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(12), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.ManganeseOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(13), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.IridiumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(14), world, random, x, y, 16, 16, 3 + random.nextInt(3), 17, 0, 70);
        }
        if (Config.GermaniumOre) {
            this.addOreSpawn(IUItem.ore.getStateFromMeta(15), world, random, x, y, 16, 16, 3 + random.nextInt(3), 15, 0, 70);
        }
        if (Config.CopperOre) {
            this.addOreSpawn(
                    IUItem.classic_ore.getStateFromMeta(0),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    4 + random.nextInt(4),
                    32,
                    0,
                    70
            );
        }
        if (Config.TinOre) {
            this.addOreSpawn(
                    IUItem.classic_ore.getStateFromMeta(1),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    4 + random.nextInt(4),
                    34,
                    0,
                    70
            );
        }
        if (Config.LeadOre) {
            this.addOreSpawn(
                    IUItem.classic_ore.getStateFromMeta(2),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(3),
                    11,
                    0,
                    70
            );
        }
        if (Config.UraniumOre) {
            this.addOreSpawn(
                    IUItem.classic_ore.getStateFromMeta(3),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    11,
                    0,
                    70
            );
        }
        if (Config.MagnetiteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(0), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.CalaveriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(1), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.GalenaOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(2), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.NickeliteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(3), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.PyriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(4), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.QuartziteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(5), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.UraniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(6), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.AzuriteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(7), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.RhodoniteOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(8), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.AlfilditOre) {
            this.addOreSpawn(IUItem.heavyore.getStateFromMeta(9), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10, 70);
        }
        if (Config.EuxeniteOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(10),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    4,
                    10,
                    70
            );
        }
        if (Config.SmithsoniteOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(11),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    4,
                    10,
                    70
            );
        }
        if (Config.IlmeniteOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(12),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    4,
                    10,
                    70
            );
        }
        if (Config.TodorokiteOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(13),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    4,
                    10,
                    70
            );
        }
        if (Config.FerroaugiteOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(14),
                    world,
                    random,
                    x,
                    y,
                    16,
                    16,
                    3 + random.nextInt(2),
                    4,
                    10,
                    70
            );
        }
        if (Config.SheeliteeOre) {
            this.addOreSpawn(
                    IUItem.heavyore.getStateFromMeta(15), world, random, x, y, 16, 16, 3 + random.nextInt(2), 4, 10,
                    70
            );
        }
        if (Config.AmericiumOre) {
            this.addOreSpawn(
                    IUItem.radiationore.getStateFromMeta(0), world, random, x, y, 16, 16, 2 + random.nextInt(2), 10, 10,
                    70
            );
        }
        if (Config.NeptuniumOre) {
            this.addOreSpawn(
                    IUItem.radiationore.getStateFromMeta(1), world, random, x, y, 16, 16, 2 + random.nextInt(2), 8, 10,
                    70
            );
        }
        if (Config.CuriumOre) {
            this.addOreSpawn(
                    IUItem.radiationore.getStateFromMeta(2), world, random, x, y, 16, 16, 2 + random.nextInt(2), 6, 10,
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

    private enum TypeOre {
        COPPER(13, 7, 0, 68, OreDistribution.UNIFORM),
        TIN(13, 6, 0, 40, OreDistribution.UNIFORM),
        LEAD(8, 4, 0, 64, OreDistribution.UNIFORM),
        URANIUM(23, 3, 0, 64, OreDistribution.UNIFORM);
        private final int count;
        private final int minHeight;
        private final int size;
        private final int maxHeight;
        private final OreDistribution oreDistribution;

        TypeOre(int count, int size, int minHeight, int maxHeight, OreDistribution oreDistribution) {
            this.count = count;
            this.size = size;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.oreDistribution = oreDistribution;
        }

        public int getCount() {
            return count;
        }

        public int getMinHeight() {
            return minHeight;
        }

        public int getSize() {
            return size;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public OreDistribution getOreDistribution() {
            return oreDistribution;
        }
    }

    private enum OreDistribution {
        UNIFORM("uniform"),
        TRIANGLE("triangle"),
        RAMP("ramp"),
        REVRAMP("revramp"),
        SMOOTH("smooth");

        private static final OreDistribution[] values = values();
        final String name;

        OreDistribution(String name) {
            this.name = name;
        }

        public static OreDistribution of(String name) {
            OreDistribution[] var1 = values;
            int var2 = var1.length;

            for (OreDistribution value : var1) {
                if (value.name.equalsIgnoreCase(name)) {
                    return value;
                }
            }

            throw new RuntimeException("Invalid/unknown worldgen distribution configured: " + name);
        }
    }

}


