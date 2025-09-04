package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.blocks.*;
import com.denfop.world.vein.AlgorithmVein;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.TypeVein;
import com.denfop.world.vein.VeinType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;

import static com.denfop.blocks.BlocksRadiationOre.BOOL_PROPERTY;
import static com.denfop.register.Register.FEATURES;

public class WorldBaseGen {
    public static DeferredHolder<Feature<?>, HiveGenerator> GEN_HIVE_GENERATOR;
    public static DeferredHolder<Feature<?>, RubTreeFeature> RUB_TREE_GENERATOR;


    public static List<VeinType> veinTypes = new ArrayList<>();
    public static List<VeinType> veinTypes1 = new ArrayList<>();
    public static Random random = new Random();
    public static DeferredHolder<Feature<?>, AlgorithmVein> VEIN_GENERATOR;
    public static DeferredHolder<Feature<?>, WorldGenGas> GEN_GAS_GENERATOR;
    public static DeferredHolder<Feature<?>, WorldGenVolcano> VOLCANO_GENERATOR;
    public static DeferredHolder<Feature<?>, WorldGenOil> OIL_GENERATOR;
    public static Map<Integer, BlockState> blockStateMap = new HashMap<>();
    public static Map<BlockState, Integer> idToblockStateMap = new HashMap<>();
    public static int id;

    public WorldBaseGen() {
        NeoForge.EVENT_BUS.register(this);
        WorldGenGas.registerFluid();


        RUB_TREE_GENERATOR = FEATURES.register("rub_tree",
                () -> new RubTreeFeature(NoneFeatureConfiguration.CODEC));
        VEIN_GENERATOR = FEATURES.register("vein",
                () -> new AlgorithmVein(NoneFeatureConfiguration.CODEC));
        GEN_GAS_GENERATOR = FEATURES.register("gen_gas",
                () -> new WorldGenGas(NoneFeatureConfiguration.CODEC));
        GEN_HIVE_GENERATOR = FEATURES.register("gen_hive",
                () -> new HiveGenerator(NoneFeatureConfiguration.CODEC));
        VOLCANO_GENERATOR = FEATURES.register("volcano",
                () -> new WorldGenVolcano(NoneFeatureConfiguration.CODEC));
        OIL_GENERATOR = FEATURES.register("oil",
                () -> new WorldGenOil(NoneFeatureConfiguration.CODEC));



        /*
        RUB_TREE = CONFIGURED_FEATURES.register("rub_tree",
                () -> new ConfiguredFeature<>(RUB_TREE_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
        RUB_TREE_PLACER = PLACED_FEATURES.register(
                "rub_tree_placed",
                () -> new PlacedFeature(
                        RUB_TREE.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );

        VEIN = CONFIGURED_FEATURES.register("vein",
                () -> new ConfiguredFeature<>(VEIN_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
       VEIN_PLACER = PLACED_FEATURES.register(
                "vein_placed",
                () -> new PlacedFeature(
                        VEIN.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );
         GEN_GAS = CONFIGURED_FEATURES.register("gen_gas",
                () -> new ConfiguredFeature<>(GEN_GAS_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
        GEN_HIVE = CONFIGURED_FEATURES.register("gen_hive",
                () -> new ConfiguredFeature<>(GEN_HIVE_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
       GEN_GAS_PLACER = PLACED_FEATURES.register(
                "gen_gas_placed",
                () -> new PlacedFeature(
                        GEN_GAS.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );
       GEN_HIVE_PLACER = PLACED_FEATURES.register(
                "gen_hive_placed",
                () -> new PlacedFeature(
                        GEN_HIVE.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );
        VOLCANO = CONFIGURED_FEATURES.register("volcano",
                () -> new ConfiguredFeature<>(VOLCANO_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
       VOLCANO_PLACER = PLACED_FEATURES.register(
                "volcano_placed",
                () -> new PlacedFeature(
                        VOLCANO.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );
        OIL = CONFIGURED_FEATURES.register("oil",
                () -> new ConfiguredFeature<>(OIL_GENERATOR.get(), NoneFeatureConfiguration.INSTANCE));
     OIL_PLACER = PLACED_FEATURES.register(
                "oil_placed",
                () -> new PlacedFeature(
                        OIL.getHolder().orElseThrow(),
                        List.of(
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                BiomeFilter.biome()
                        )
                )
        );
        CALCIUM = CONFIGURED_FEATURES.register("calcium", () -> new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(RuleBasedBlockStateProvider.simple(IUItem.ore2.getStateFromMeta(7).getBlock()), BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, IUItem.ore2.getStateFromMeta(7).getBlock())), UniformInt.of(3, 4), 1)));
       SALTPETER = CONFIGURED_FEATURES.register("saltpeter", () -> new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(RuleBasedBlockStateProvider.simple(IUItem.ore2.getStateFromMeta(6).getBlock()), BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, IUItem.ore2.getStateFromMeta(6).getBlock())), UniformInt.of(3, 4), 1)));
      PEAT = CONFIGURED_FEATURES.register("peat", () -> new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(RuleBasedBlockStateProvider.simple(IUItem.blockResource.getStateFromMeta(10).getBlock()), BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, IUItem.blockResource.getStateFromMeta(9).getBlock())), UniformInt.of(2, 3), 1)));
         CALCIUM_PLACER = PLACED_FEATURES.register(
                "calcium_placed",
                () -> new PlacedFeature(
                        CALCIUM.getHolder().orElseThrow(),
                        List.of(
                                PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)), BiomeFilter.biome()
                        )
                )
        );
        SALTPETER_PLACER = PLACED_FEATURES.register(
                "saltpeter_placed",
                () -> new PlacedFeature(
                        SALTPETER.getHolder().orElseThrow(),
                        List.of(
                                PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)), BiomeFilter.biome()
                        )
                )
        );
      PEAT_PLACER = PLACED_FEATURES.register(
                "peat_placed",
                () -> new PlacedFeature(
                        PEAT.getHolder().orElseThrow(),
                        List.of(
                                PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)), BiomeFilter.biome()
                        )
                )
        );
        */
    }

    public static void initVein() {
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(0)).get(), 0, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.defaultBlockState(), 60, 0),
                        new ChanceOre(Blocks.GOLD_ORE.defaultBlockState(), 25, 0),
                        new ChanceOre(IUItem.ore2.getBlockState(4), 15, 4),
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(1)).get(), 1, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlock(BlockOre.Type.getFromID(7)).get().defaultBlockState(), 28, 7),
                        new ChanceOre(Blocks.GOLD_ORE.defaultBlockState(), 44, 0),
                        new ChanceOre(Blocks.COPPER_ORE.defaultBlockState(), 28, 0)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(2)).get(), 2, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(11), 26, 11),
                        new ChanceOre(IUItem.classic_ore.getBlockState(2), 74, 2)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(3)).get(), 3, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(8), 44, 8),
                        new ChanceOre(IUItem.ore.getBlockState(6), 56, 6)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(4)).get(), 4, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.defaultBlockState(), 80, 0),
                        new ChanceOre(IUItem.ore.getBlockState(4), 20, 4)}

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(5)).get(), 5, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(12), 16, 12),
                        new ChanceOre(IUItem.ore.getBlockState(11), 26, 11),
                        new ChanceOre(IUItem.classic_ore.getBlockState(1), 24, 1),
                        new ChanceOre(IUItem.ore.getBlockState(10), 34, 10),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(6)).get(), 6, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.classic_ore.getBlockState(3).setValue(BlockClassicOre.BOOL_PROPERTY, true), 60, 3),
                        new ChanceOre(IUItem.toriyore.getBlockState(0).setValue(BlockThoriumOre.BOOL_PROPERTY, true), 32, 0),
                        new ChanceOre(IUItem.radiationore.getBlockState(1).setValue(BOOL_PROPERTY, true), 4, 1),
                        new ChanceOre(IUItem.radiationore.getBlockState(0).setValue(BOOL_PROPERTY, true), 3, 0),
                        new ChanceOre(IUItem.radiationore.getBlockState(2).setValue(BOOL_PROPERTY, true), 1, 2),
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(7)).get(), 7, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.COPPER_ORE.defaultBlockState(), 55, 0),
                        new ChanceOre(Blocks.LAPIS_ORE.defaultBlockState(), 23, 0),
                        new ChanceOre(Blocks.REDSTONE_ORE.defaultBlockState(), 21, 0)
                }

        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(8)).get(), 8, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(13), 44, 13),
                        new ChanceOre(IUItem.ore.getBlockState(5), 28, 5),
                        new ChanceOre(IUItem.ore.getBlockState(0), 28, 0),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(9)).get(), 9, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(4), 50, 4),
                        new ChanceOre(IUItem.ore.getBlockState(6), 50, 6),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(10)).get(), 10, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(8), 50, 8),
                        new ChanceOre(IUItem.classic_ore.getBlockState(3).setValue(BlockClassicOre.BOOL_PROPERTY, true), 25, 3),
                        new ChanceOre(IUItem.toriyore.getDefaultState().setValue(BlockThoriumOre.BOOL_PROPERTY, true), 25, 0),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(11)).get(), 11, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(12), 65, 12),
                        new ChanceOre(Blocks.COAL_ORE.defaultBlockState(), 35, 0)
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(12)).get(), 12, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(8), 47, 8),
                        new ChanceOre(Blocks.IRON_ORE.defaultBlockState(), 33, 0),
                        new ChanceOre(IUItem.ore.getBlockState(2), 33, 2),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(13)).get(), 13, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(IUItem.ore.getBlockState(13), 66, 13),
                        new ChanceOre(IUItem.ore.getBlockState(5), 17, 5),
                        new ChanceOre(IUItem.ore.getBlockState(1), 17, 1),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(14)).get(), 14, TypeVein.SMALL,
                new ChanceOre[]{new ChanceOre(Blocks.IRON_ORE.defaultBlockState(), 30, 0),
                        new ChanceOre(IUItem.ore.getBlockState(5), 40, 5),
                        new ChanceOre(IUItem.ore.getBlockState(1), 30, 1),
                }
        ));
        veinTypes.add(new VeinType(IUItem.heavyore.getBlock(BlockHeavyOre.Type.getFromID(15)).get(), 15, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getBlockState(3), 80, 3),
                        new ChanceOre(IUItem.classic_ore.getBlockState(1), 20, 1),
                }
        ));

        veinTypes.add(new VeinType(null, 16, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getBlockState(14), 70, 14),
                        new ChanceOre(IUItem.ore.getBlockState(7), 30, 7),
                }
        ));

        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(3)).get(), 3, 17, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getBlockState(15), 60, 15),
                        new ChanceOre(Blocks.COPPER_ORE.defaultBlockState(), 20, 0),
                        new ChanceOre(IUItem.ore3.getBlockState(4), 20, 4),
                }
        ));

        veinTypes.add(new VeinType(null, 18, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore.getBlockState(2), 50, 2),
                        new ChanceOre(IUItem.ore.getBlockState(9), 50, 9),
                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(7)).get(), 7, 19, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore2.getBlockState(3), 65, 3),
                        new ChanceOre(IUItem.ore.getBlockState(14), 35, 14),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(4)).get(), 4, 20, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.ore2.getBlockState(4), 50, 4),
                        new ChanceOre(IUItem.ore.getBlockState(13), 25, 13),
                        new ChanceOre(IUItem.ore3.getBlockState(9), 25, 9),
                }
        ));

        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(8)).get(), 8, 21, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.ore2.getBlockState(5), 70, 5),
                        new ChanceOre(IUItem.ore3.getBlockState(0), 30, 0),
                }
        ));
        veinTypes.add(new VeinType(null, 22, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(Blocks.COPPER_ORE.defaultBlockState(), 72, 0),
                        new ChanceOre(IUItem.ore.getBlockState(9), 28, 9),
                }
        ));

        veinTypes.add(new VeinType(null, 23, TypeVein.SMALL,
                new ChanceOre[]{

                        new ChanceOre(IUItem.classic_ore.getBlockState(1), 70, 1),
                        new ChanceOre(IUItem.ore.getBlockState(11), 30, 11),
                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(0)).get(), 0, 24, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getBlockState(0), 55, 0),
                        new ChanceOre(Blocks.IRON_ORE.defaultBlockState(), 35, 0),
                        new ChanceOre(IUItem.ore.getBlockState(6), 10, 6),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(1)).get(), 1, 25, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getBlockState(10), 60, 10),
                        new ChanceOre(IUItem.ore3.getBlockState(11), 40, 11),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(2)).get(), 2, 26, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getBlockState(7), 50, 7),
                        new ChanceOre(IUItem.ore.getBlockState(3), 50, 3),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(6)).get(), 6, 27, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(6), 70, 6),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(3), 30, 3)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(9)).get(), 9, 28, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(12), 50, 12),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(2), 20, 2),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(0), 30, 0),

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(5)).get(), 5, 29, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(13), 50, 13),
                        new ChanceOre(IUItem.classic_ore.getStateFromMeta(2), 50, 2)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(12)).get(), 12, 30, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(14), 70, 14),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(5), 30, 5)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(11)).get(), 11, 31, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(IUItem.ore3.getStateFromMeta(12), 70, 12),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(1), 30, 1)

                }
        ));
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(10)).get(), 10, 32, TypeVein.SMALL,
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
        veinTypes.add(new VeinType(IUItem.mineral.getBlock(BlockMineral.Type.getFromID(13)).get(), 13, 34, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(Blocks.COAL_ORE.defaultBlockState(), 38, 0),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(3), 15, 3),
                        new ChanceOre(IUItem.ore.getStateFromMeta(10), 15, 10),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(0), 9, 0),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(1), 9, 1),
                        new ChanceOre(IUItem.preciousore.getStateFromMeta(2), 9, 2),
                        new ChanceOre(IUItem.ore3.getStateFromMeta(15), 5, 15),

                }
        ));
        veinTypes.add(new VeinType(null, 0, 35, TypeVein.SMALL,
                new ChanceOre[]{
                        new ChanceOre(Blocks.COAL_ORE.defaultBlockState(), 100, 0),

                }
        ));
        id = 0;
        for (VeinType veinType : veinTypes) {
            if (veinType.getHeavyOre() != null) {
                BlockState state = veinType.getHeavyOre().getBlock().defaultBlockState();
                if (!idToblockStateMap.containsKey(state)) {
                    idToblockStateMap.put(state, id);
                    blockStateMap.put(id, state);
                    id++;
                }
            }
            for (ChanceOre chanceOre : veinType.getOres()) {
                BlockState state = chanceOre.getBlock();
                if (!idToblockStateMap.containsKey(state)) {
                    idToblockStateMap.put(state, id);
                    blockStateMap.put(id, state);
                    id++;
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide() && event.getLevel().dimension() == Level.OVERWORLD) {
            if (!WorldGenVolcano.generatorVolcanoList.isEmpty()) {
                GeneratorVolcano generatorVolcano = WorldGenVolcano.generatorVolcanoList.get(0);
                generatorVolcano.setWorld(event.getLevel());
                generatorVolcano.generate();
                if (generatorVolcano.isEnd()) {
                    WorldGenVolcano.generatorVolcanoList.remove(0);
                }
            }

        }
    }
}
