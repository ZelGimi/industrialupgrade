package com.denfop.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IULootTableProvider extends LootTableProvider {

    public IULootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(IUChestLoot::new, LootContextParamSets.CHEST)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> tables, ValidationContext validationContext) {
        for (Map.Entry<ResourceLocation, LootTable> entry : tables.entrySet()) {
            ResourceLocation id = entry.getKey();
            LootTable table = entry.getValue();

            if ("minecraft".equals(id.getNamespace())) {
                throw new IllegalStateException(
                        "Do not generate vanilla loot table directly: " + id +
                                ". Use industrialupgrade:inject/... table + Global Loot Modifier instead."
                );
            }

            LootTables.validate(validationContext, id, table);
        }
    }

    @Override
    public String getName() {
        return "Industrial Upgrade Loot Tables";
    }
}