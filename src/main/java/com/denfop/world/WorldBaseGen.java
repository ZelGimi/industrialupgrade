package com.denfop.world;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.blocks.BlockThoriumOre;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.FluidName;
import com.denfop.world.vein.AlgorithmVein;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.denfop.blocks.BlockClassicOre.BOOL_PROPERTY;

public class WorldBaseGen implements IWorldGenerator {


    public static List<VeinType> veinTypes = new ArrayList<>();
    public static List<VeinType> veinTypes1 = new ArrayList<>();
    public static Random random = new Random();
    final Fluid fluid = FluidName.fluidneft.getInstance();
    final Block block = fluid.getBlock();

    public static void init() {
        GameRegistry.registerWorldGenerator(new WorldBaseGen(), 0);
        GameRegistry.registerWorldGenerator(new HiveGenerator(IUItem.hive), 0);
        WorldGenGas.registerFluid();
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
        if (Config.DimensionList.contains(world.provider.getDimension())) {
            final BlockPos chunkPos = new BlockPos(chunkX << 4, 0,
                    chunkZ << 4
            );
            net.minecraft.util.math.ChunkPos forgeChunkPos = new net.minecraft.util.math.ChunkPos(chunkPos);
            if (net.minecraftforge.event.terraingen.TerrainGen.decorate(world, random, forgeChunkPos,
                    net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY
            ) && WorldBaseGen.random.nextInt(100) >= 70) {
                if (world.getBiome(new BlockPos(chunkX * 16, 0,
                        chunkZ * 16
                )) != Biome.getBiome(6)) {
                    if (WorldBaseGen.random.nextInt(100) > 39) {
                        for (int i1 = 0; i1 < 1; ++i1) {

                            int l1 = random.nextInt(16) + 8;
                            int i6 = random.nextInt(16) + 8;
                            new WorldGenSandsOres(random.nextInt(4) + 4, IUItem.ore2.getStateFromMeta(7)).generate(world, random,
                                    world.getTopSolidOrLiquidBlock(chunkPos.add(l1, 0, i6))
                            );
                        }
                    } else {
                        for (int i1 = 0; i1 < 1; ++i1) {

                            int l1 = random.nextInt(16) + 8;
                            int i6 = random.nextInt(16) + 8;
                            new WorldGenSandsOres(random.nextInt(4) + 4, IUItem.ore2.getStateFromMeta(6)).generate(world, random,
                                    world.getTopSolidOrLiquidBlock(chunkPos.add(l1, 0, i6))
                            );
                        }
                    }
                } else {
                    if (WorldBaseGen.random.nextInt(100) > 39) {
                        for (int i1 = 0; i1 < 1; ++i1) {

                            int l1 = random.nextInt(16) + 8;
                            int i6 = random.nextInt(16) + 8;
                            new WorldGenSandsOres(random.nextInt(4) + 4, IUItem.blockResource.getStateFromMeta(10)).generate(
                                    world,
                                    random,
                                    world.getTopSolidOrLiquidBlock(chunkPos.add(l1, 0, i6))
                            );
                        }
                    }
                }
            }

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
                        new WorldGenOil(block)
                                .generate(world, random, new BlockPos(var3, var4, var5));
                    }

                }
            }

            if (WorldBaseGen.random.nextInt(900) + 1 > 500) {
                final Biome biome = world.getBiome(new BlockPos(chunkX * 16, 0,
                        chunkZ * 16
                ));
                var3 = chunkX * 16 + random.nextInt(16) + 8;
                var4 = random.nextInt(random.nextInt(random.nextInt(30) + 20) + 8);
                var5 = chunkZ * 16 + random.nextInt(16) + 8;
                if (biome instanceof BiomeOcean || biome instanceof BiomeRiver) {
                    final int rand = WorldBaseGen.random.nextInt(100);
                    TypeGas typeGas;
                    if (rand < 50) {
                        typeGas = TypeGas.GAS;
                    }  else if (rand < 75) {
                        typeGas = TypeGas.IODINE;
                    } else {
                        typeGas = TypeGas.FLUORINE;
                    }
                    new WorldGenGas(typeGas).generate(world, random, new BlockPos(var3, var4, var5));

                } else {

                    TypeGas typeGas;
                    final int rand = WorldBaseGen.random.nextInt(100);
                    if (rand < 50) {
                        typeGas = TypeGas.GAS;
                    } else if (rand < 75) {
                        typeGas = TypeGas.BROMIDE;
                    } else {
                        typeGas = TypeGas.CHLORINE;
                    }
                    new WorldGenGas(typeGas).generate(world, random, new BlockPos(var3, var4, var5));

                }
            }


            Chunk chunk = chunkProvider.provideChunk(chunkX, chunkZ);
            generateSurface(world, random, chunkX * 16, chunkZ * 16, chunkGenerator, chunkProvider, chunk);

            genRubberTree(chunk, 2);
        }
    }
    public static void initVein() {
        veinTypes.add(new VeinType(IUItem.heavyore, 0, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.getDefaultState(), 60, 0),
                        new ChanceOre(Blocks.GOLD_ORE.getDefaultState(), 25, 0),
                        new ChanceOre(IUItem.ore2.getStateFromMeta(4), 15, 4),
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 1, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(7), 28, 7),
                        new ChanceOre(Blocks.GOLD_ORE.getDefaultState(), 44, 0),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(0), 28, 0)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 2, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(11), 26, 11),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(2), 74, 2)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 3, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(8), 44, 8),
                        new ChanceOre(IUItem.ore.getStateFromMeta(6), 56, 6)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 4, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.getDefaultState(), 80, 0),
                        new ChanceOre(IUItem.ore.getStateFromMeta(4), 20, 4)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 5, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(12), 16, 12),
                        new ChanceOre(IUItem.ore.getStateFromMeta(11), 26, 11),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(1), 24, 1),
                        new ChanceOre(IUItem.ore.getStateFromMeta(10), 34, 10),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 6,true, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.classic_ore.getStateFromMeta(3).withProperty(BOOL_PROPERTY,true), 60, 3,
                        IUItem.classic_ore.getStateFromMeta(3)),
                        new ChanceOre(IUItem.toriyore.getDefaultState(), 32, 0,
                                IUItem.toriyore.getDefaultState().withProperty(BlockThoriumOre.BOOL_PROPERTY,false)),
                        new ChanceOre(IUItem.radiationore.getStateFromMeta(1), 4, 1,
                                IUItem.radiationore.getStateFromMeta(1).withProperty(BlocksRadiationOre.BOOL_PROPERTY,false)),
                        new ChanceOre(IUItem.radiationore.getStateFromMeta(0), 3, 0,
                                IUItem.radiationore.getStateFromMeta(0).withProperty(BlocksRadiationOre.BOOL_PROPERTY,false)),
                        new ChanceOre(IUItem.radiationore.getStateFromMeta(2), 1, 2,
                                IUItem.radiationore.getStateFromMeta(2).withProperty(BlocksRadiationOre.BOOL_PROPERTY,false)),
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 7, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.classic_ore.getStateFromMeta(0), 55, 0),
                        new ChanceOre(Blocks.LAPIS_ORE.getDefaultState(), 23, 0),
                        new ChanceOre(Blocks.REDSTONE_ORE.getDefaultState(), 21, 0)
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 8, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(13), 44, 13),
                        new ChanceOre(IUItem.ore.getStateFromMeta(5), 28, 5),
                        new ChanceOre(IUItem.ore.getStateFromMeta(0), 28, 0),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 9, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(4), 50, 4),
                        new ChanceOre(IUItem.ore.getStateFromMeta(6), 50, 6),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 10, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(8), 50, 8),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(3).withProperty(BOOL_PROPERTY,true), 25, 3,
                                IUItem.classic_ore.getStateFromMeta(3).withProperty(BOOL_PROPERTY,false)),
                        new ChanceOre(IUItem.toriyore.getDefaultState(), 25, 0,  IUItem.toriyore.getDefaultState().withProperty(BlockThoriumOre.BOOL_PROPERTY,false)),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 11, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(12), 65, 12),
                        new ChanceOre(Blocks.COAL_ORE.getDefaultState(), 35, 0)
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 12, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(8), 47, 8),
                        new ChanceOre(Blocks.IRON_ORE.getDefaultState(), 33, 0),
                        new ChanceOre(IUItem.ore.getStateFromMeta(2), 33, 2),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 13, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getStateFromMeta(13), 66, 13),
                        new ChanceOre(IUItem.ore.getStateFromMeta(5), 17, 5),
                        new ChanceOre(IUItem.ore.getStateFromMeta(1), 17, 1),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 14, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.getDefaultState(), 30, 0),
                        new ChanceOre(IUItem.ore.getStateFromMeta(5), 40, 5),
                        new ChanceOre(IUItem.ore.getStateFromMeta(1), 30, 1),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore, 15, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getStateFromMeta(3), 80, 3),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(1), 20, 1),
                }
        ));

        veinTypes.add(new VeinType(null, 16, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getStateFromMeta(14), 70, 14),
                        new ChanceOre(IUItem.ore.getStateFromMeta(7), 30, 7),
                }
        ));

        veinTypes.add(new VeinType(IUItem.mineral, 3, 17, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getStateFromMeta(15), 60, 15),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(0), 20, 0),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(4), 20, 4),
                }
        ));

        veinTypes.add(new VeinType(null, 18, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getStateFromMeta(2), 50, 2),
                        new ChanceOre(IUItem.ore.getStateFromMeta(9), 50, 9),
                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 7, 19, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore2.getStateFromMeta(3), 65, 3),
                        new ChanceOre(IUItem.ore.getStateFromMeta(14), 35, 14),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 4, 20, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.ore2.getStateFromMeta(4), 50, 4),
                        new ChanceOre(IUItem.ore.getStateFromMeta(13), 25, 13),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(9), 25, 9),
                }
        ));

        veinTypes.add(new VeinType(IUItem.mineral, 8, 21, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.ore2.getStateFromMeta(5), 70, 5),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(0), 30, 0),
                }
        ));
        veinTypes.add(new VeinType(null, 22, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(0), 72, 0),
                        new ChanceOre(IUItem.ore.getStateFromMeta(9), 28, 9),
                }
        ));

        veinTypes.add(new VeinType(null, 23, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(1), 70, 1),
                        new ChanceOre(IUItem.ore.getStateFromMeta(11), 30, 11),
                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 0, 24, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(0), 55, 0),
                        new ChanceOre(Blocks.IRON_ORE.getDefaultState(), 35, 0),
                        new ChanceOre(IUItem.ore.getStateFromMeta(6), 10, 6),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 1, 25, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(10), 60, 10),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(11), 40, 11),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 2, 26, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(7), 50, 7),
                        new ChanceOre(IUItem.ore.getStateFromMeta(3), 50, 3),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 6, 27, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(6), 70, 6),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(3), 30, 3)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 9, 28, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(12), 50, 12),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(2), 20, 2),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(0), 30, 0),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 5, 29, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(13), 50, 13),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(2), 50, 2)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 12, 30, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(14), 70, 14),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(5), 30, 5)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 11, 31, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(12), 70, 12),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(1), 30, 1)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 10, 32, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(8), 70, 8),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(9), 30, 9)

                }
        ));
        veinTypes.add(new VeinType(null, 0, 33, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.apatite.getStateFromMeta(0), 30, 0),
                        new ChanceOre(IUItem.apatite.getStateFromMeta(1), 30, 1),
                        new ChanceOre(IUItem.apatite.getStateFromMeta(2), 18, 2),
                        new ChanceOre(IUItem.apatite.getStateFromMeta(3), 11, 3),
                        new ChanceOre(IUItem.apatite.getStateFromMeta(4), 11, 4),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral, 13, 34, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(Blocks.COAL_ORE.getDefaultState(), 35, 0),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(3), 30, 3),
                        new ChanceOre(IUItem.ore.getStateFromMeta(10), 25, 10),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(15), 10, 15),

                }
        ));
        veinTypes.add(new VeinType(null, 0, 35, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(Blocks.COAL_ORE.getDefaultState(), 100, 0),

                }
        ));
        veinTypes.add(new VeinType(null, 0, 36, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(Blocks.REDSTONE_ORE.getDefaultState(), 61, 0),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(0), 13, 0),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(1), 13, 1),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(2), 13, 2),

                }
        ));
    }

    private static int randomX(Chunk chunk, Random rnd) {
        return chunk.x * 16 + rnd.nextInt(16);
    }

    private static int randomZ(Chunk chunk, Random rnd) {
        return chunk.z * 16 + rnd.nextInt(16);
    }

    private static void genRubberTree(Chunk chunk, float baseScale) {
        Biome[] biomes = new Biome[6];

        int rubberTrees;
        int i;
        for (rubberTrees = 0; rubberTrees < 5; ++rubberTrees) {
            int x = chunk.x * 16 + 8 + (rubberTrees & 2) * 15;
            i = chunk.z * 16 + 8 + ((rubberTrees & 2) >>> 1) * 15;
            BlockPos pos = new BlockPos(x, chunk.getWorld().getSeaLevel(), i);
            biomes[rubberTrees] = chunk.getWorld().getBiomeProvider().getBiome(pos
                    , Biomes.PLAINS);
        }

        rubberTrees = 0;

        for (Biome biome : biomes) {
            if (biome != null) {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    rubberTrees += random.nextInt(10) + 2;
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                    rubberTrees += random.nextInt(15) + 5;
                }
                if (BiomeDictionary.hasType(
                        biome,
                        BiomeDictionary.Type.FOREST
                )) {
                    rubberTrees += random.nextInt(5) + 1;
                }
            }
        }

        rubberTrees = Math.round((float) rubberTrees * baseScale);
        rubberTrees /= 2;
        if (rubberTrees > 0 && random.nextInt(100) < rubberTrees) {
            WorldGenRubTree gen = new WorldGenRubTree(false);
            for (i = 0; i < rubberTrees; ++i) {
                if (net.minecraftforge.event.terraingen.TerrainGen.decorate(chunk.getWorld(), random, chunk.getPos(),
                        DecorateBiomeEvent.Decorate.EventType.TREE
                )) {
                    if (!gen.generate(
                            chunk.getWorld(),
                            random,
                            new BlockPos(randomX(chunk, random), chunk.getWorld().getSeaLevel(), randomZ(chunk, random))
                    )) {
                        rubberTrees -= 3;
                    }
                }
            }
        }


    }

    public static void remove(Block block) {
        final Iterator<VeinType> iter = veinTypes.iterator();
        while (iter.hasNext()) {
            final VeinType vein = iter.next();
            if (vein.getHeavyOre() != null && vein.getHeavyOre().getBlock() == block) {
                iter.remove();
            } else {
                vein.getOres().removeIf(chanceOre -> chanceOre.getBlock().getBlock() == block);
            }
        }
    }



    private void generateSurface(
            World world, Random random, int x, int y,
            final IChunkGenerator chunkGenerator,
            final IChunkProvider chunkProvider,
            final Chunk chunk
    ) {


        if (WorldBaseGen.random.nextInt(4) <= 2) {
            if (veinTypes1.isEmpty()) {
                veinTypes1 = new ArrayList<>(veinTypes);
            }
            int meta = WorldBaseGen.random.nextInt(veinTypes1.size());
            final VeinType veinType = veinTypes1.remove(meta);
            AlgorithmVein.generate(world, veinType,
                    new BlockPos(x + random.nextInt(16), 2, y + random.nextInt(16)), chunk, veinType.getDeposits_meta()
            );
        }

        final BlockPos pos1 = new BlockPos(x + random.nextInt(16), 2, y + random.nextInt(16));
        final Biome biome = chunk.getWorld().getBiomeProvider().getBiome(pos1
                ,
                Biomes.PLAINS
        );
        if (biome instanceof BiomeHills) {
            if (WorldBaseGen.random.nextInt(100) == 95) {
                if (WorldBaseGen.random.nextInt(100) >= 60) {
                    new WorldGenVolcano().generate(world, world.rand, pos1);
                }
            }
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


