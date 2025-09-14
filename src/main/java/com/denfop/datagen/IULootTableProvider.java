package com.denfop.datagen;

import com.denfop.Constants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IULootTableProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> industrialSubProviders = ImmutableList.of(Pair.of(IUChestLoot::new, LootContextParamSets.CHEST));
    private final PackOutput.PathProvider pathProvider;
    public static final ResourceLocation VOLCANO_LOOT_TABLE =  ResourceLocation.tryBuild(Constants.MOD_ID, "chests/volcano");

    public IULootTableProvider(PackOutput pOutput) {
        this.pathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        industrialSubProviders.forEach((p_124458_) -> {
            p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
                if (map.put(p_176077_, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_176077_);
                }
            });
        });
        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {
            @Nullable
            public <T> T getElement(LootDataId<T> p_279283_) {
                return (T)(p_279283_.type() == LootDataType.TABLE ? map.get(p_279283_.location()) : null);
            }
        });



        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
                LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf(map.entrySet().stream().map((p_278900_) -> {
                ResourceLocation resourcelocation1 = p_278900_.getKey();
                LootTable loottable = p_278900_.getValue();
                Path path = this.pathProvider.json(resourcelocation1);
                return DataProvider.saveStable(pOutput, LootDataType.TABLE.parser().toJsonTree(loottable), path);
            }).toArray(CompletableFuture[]::new));
        }
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }
}
