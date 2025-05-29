package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.componets.Fluids;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> RUB_TREE_PLACED = registerKey("rub_tree_placed");
    public static final ResourceKey<PlacedFeature> VEIN_PLACED = registerKey("vein_placed");
    public static final ResourceKey<PlacedFeature> GEN_GAS_PLACED = registerKey("gen_gas_placed");
    public static final ResourceKey<PlacedFeature> GEN_HIVE_PLACED = registerKey("gen_hive_placed");
    public static final ResourceKey<PlacedFeature> VOLCANO_PLACED = registerKey("volcano_placed");
    public static final ResourceKey<PlacedFeature> OIL_PLACED = registerKey("oil_placed");
    public static final ResourceKey<PlacedFeature> CALCIUM_PLACED = registerKey("calcium_placed");
    public static final ResourceKey<PlacedFeature> SALTPETER_PLACED = registerKey("saltpeter_placed");
    public static final ResourceKey<PlacedFeature> PEAT_PLACED = registerKey("peat_placed");

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Constants.MOD_ID, name));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        registerPlaced(context, RUB_TREE_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.RUB_TREE));
        registerPlaced(context, VEIN_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.VEIN));
        registerPlaced(context, GEN_GAS_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.GEN_GAS));
        registerPlaced(context, GEN_HIVE_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.GEN_HIVE));
        registerPlaced(context, VOLCANO_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.VOLCANO));
        registerPlaced(context, OIL_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.OIL));
        registerPlaced(context, CALCIUM_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.CALCIUM),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,InSquarePlacement.spread(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome());
        registerPlaced(context, SALTPETER_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.SALTPETER),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,InSquarePlacement.spread(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome());
        registerPlaced(context, PEAT_PLACED, configuredFeatures.getOrThrow(ConfiguredFeaturesGen.PEAT),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,InSquarePlacement.spread(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome());
    }

    private static void registerPlaced(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                       Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... modifiers) {
        context.register(key, new PlacedFeature(configuredFeature, List.of(modifiers)));
    }

    private static void registerPlaced(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                       Holder<ConfiguredFeature<?, ?>> configuredFeature) {
        context.register(key, new PlacedFeature(configuredFeature, List.of(
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        )));
    }
}
