package com.denfop.datagen;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IULootTableProvider extends LootTableProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> industrialSubProviders = ImmutableList.of(Pair.of(IUChestLoot::new, LootContextParamSets.CHEST));
    private final DataGenerator.PathProvider pathProvider;
    public static final ResourceLocation VOLCANO_LOOT_TABLE =  ResourceLocation.tryBuild(Constants.MOD_ID, "chests/volcano");

    public IULootTableProvider(DataGenerator pGenerator) {
        super(pGenerator);
        this.pathProvider = pGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "loot_tables");
    }

    @Override
    public void run(CachedOutput pOutput) {
        Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        industrialSubProviders.forEach((p_124458_) -> {
            p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
                if (map.put(p_176077_, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_176077_);
                }
            });
        });
        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, (p_124465_) -> null, map::get);


        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
              LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            map.forEach((p_236272_, p_236273_) -> {
                Path path = this.pathProvider.json(p_236272_);

                try {
                    DataProvider.saveStable(pOutput, LootTables.serialize(p_236273_), path);
                } catch (IOException ioexception) {
                  //  LOGGER.error("Couldn't save loot table {}", path, ioexception);
                }

            });
        }
    }
}
