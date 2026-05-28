package com.denfop.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IULootTableProvider implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> industrialSubProviders =
            ImmutableList.of(
                    Pair.of(IUChestLoot::new, LootContextParamSets.CHEST)
            );

    private final PackOutput.PathProvider pathProvider;

    public IULootTableProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation, LootTable> tables = Maps.newHashMap();

        for (Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet> pair : industrialSubProviders) {
            Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> supplier = pair.getFirst();
            LootContextParamSet paramSet = pair.getSecond();

            supplier.get().accept((id, builder) -> {
                if ("minecraft".equals(id.getNamespace())) {
                    throw new IllegalStateException(
                            "Do not generate vanilla loot table directly: " + id +
                                    ". Use industrialupgrade:inject/... table + Global Loot Modifier instead."
                    );
                }

                LootTable previous = tables.put(id, builder.setParamSet(paramSet).build());
                if (previous != null) {
                    throw new IllegalStateException("Duplicate loot table " + id);
                }
            });
        }

        ValidationContext validationContext = new ValidationContext(
                LootContextParamSets.ALL_PARAMS,
                new LootDataResolver() {
                    @Nullable
                    @Override
                    @SuppressWarnings("unchecked")
                    public <T> T getElement(LootDataId<T> id) {
                        return id.type() == LootDataType.TABLE
                                ? (T) tables.get(id.location())
                                : null;
                    }
                }
        );

        Multimap<String, String> problems = validationContext.getProblems();
        if (!problems.isEmpty()) {
            problems.forEach((table, problem) ->
                    LOGGER.warn("Found validation problem in {}: {}", table, problem)
            );
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        }

        return CompletableFuture.allOf(
                tables.entrySet()
                        .stream()
                        .map(entry -> {
                            ResourceLocation id = entry.getKey();
                            LootTable table = entry.getValue();
                            Path path = this.pathProvider.json(id);

                            return DataProvider.saveStable(
                                    output,
                                    LootDataType.TABLE.parser().toJsonTree(table),
                                    path
                            );
                        })
                        .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName() {
        return "Industrial Upgrade Loot Tables";
    }
}