package com.denfop.datagen;

import com.denfop.Constants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IULootTableProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceKey<LootTable>, LootTable.Builder>>>, LootContextParamSet>> industrialSubProviders = ImmutableList.of(Pair.of(IUChestLoot::new, LootContextParamSets.CHEST));
    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

 public  static    ResourceKey<LootTable>  VOLCANO_LOOT_TABLE =   ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.tryBuild(Constants.MOD_ID, "chests/volcano"));
    public IULootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> p_323798_) {
        this.pathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.registries = p_323798_;
    }
    private static ResourceLocation sequenceIdForLootTable(ResourceKey<LootTable> p_336172_) {
        return p_336172_.location();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        final Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        WritableRegistry<LootTable> writableregistry = new MappedRegistry<>(Registries.LOOT_TABLE, Lifecycle.experimental());

        industrialSubProviders.forEach((p_124458_) -> {

            p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
                ResourceLocation resourcelocation = sequenceIdForLootTable(p_176077_);

                if (map.put(resourcelocation, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_176077_);
                }
                p_176078_.setRandomSequence(resourcelocation);
                LootTable loottable = p_176078_.setParamSet(p_124458_.getSecond()).build();
                writableregistry.register(p_176077_, loottable, RegistrationInfo.BUILT_IN);
            });
        });
        writableregistry.freeze();
        ProblemReporter.Collector problemreporter$collector = new ProblemReporter.Collector();
        HolderGetter.Provider holdergetter$provider = new RegistryAccess.ImmutableRegistryAccess(List.of(writableregistry)).freeze().asGetterLookup();

        ValidationContext validationcontext = new ValidationContext(problemreporter$collector, LootContextParamSets.ALL_PARAMS, holdergetter$provider);

        Multimap<String, String> multimap =problemreporter$collector.get();
        writableregistry.holders().forEach(p_335195_ -> {
            p_335195_.value().validate(validationcontext.setParams(p_335195_.value().getParamSet()).enterElement("{" + p_335195_.key().location() + "}", p_335195_.key()));
        });
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
                 try {
                    return DataProvider.saveStable(pOutput, registries.get(), LootTable.DIRECT_CODEC, loottable, path);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).toArray(CompletableFuture[]::new));
        }
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }
}
