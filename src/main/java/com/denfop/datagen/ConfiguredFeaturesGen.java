package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

import java.util.List;

public class ConfiguredFeaturesGen {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RUB_TREE = registerKey("rub_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEIN = registerKey("vein");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GEN_GAS = registerKey("gen_gas");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GEN_HIVE = registerKey("gen_hive");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANO = registerKey("volcano");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OIL = registerKey("oil");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CALCIUM = registerKey("calcium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SALTPETER = registerKey("saltpeter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEAT = registerKey("peat");
    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Constants.MOD_ID, name));
    }
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(RUB_TREE, new ConfiguredFeature<>(WorldBaseGen.RUB_TREE_GENERATOR.get(), new NoneFeatureConfiguration()));
        context.register(VEIN, new ConfiguredFeature<>(WorldBaseGen.VEIN_GENERATOR.get(), new NoneFeatureConfiguration()));
        context.register(GEN_GAS, new ConfiguredFeature<>(WorldBaseGen.GEN_GAS_GENERATOR.get(), new NoneFeatureConfiguration()));
        context.register(GEN_HIVE, new ConfiguredFeature<>(WorldBaseGen.GEN_HIVE_GENERATOR.get(), new NoneFeatureConfiguration()));
        context.register(VOLCANO, new ConfiguredFeature<>(WorldBaseGen.VOLCANO_GENERATOR.get(), new NoneFeatureConfiguration()));
        context.register(OIL, new ConfiguredFeature<>(WorldBaseGen.OIL_GENERATOR.get(), new NoneFeatureConfiguration()));

        context.register(CALCIUM, new ConfiguredFeature<>(Feature.DISK,
                new DiskConfiguration(RuleBasedBlockStateProvider.simple(IUItem.ore2.getStateFromMeta(7).getBlock()),    BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)), UniformInt.of(3, 4), 1)));

        context.register(SALTPETER, new ConfiguredFeature<>(Feature.DISK,
                new DiskConfiguration(
                        RuleBasedBlockStateProvider.simple(IUItem.ore2.getStateFromMeta(6).getBlock()),
                        BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)),
                        UniformInt.of(3, 4), 1
                )));

        context.register(PEAT, new ConfiguredFeature<>(Feature.DISK,
                new DiskConfiguration(
                        RuleBasedBlockStateProvider.simple(IUItem.blockResource.getStateFromMeta(10).getBlock()),
                        BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)),
                        UniformInt.of(3, 4), 1
                )));
    }
}
