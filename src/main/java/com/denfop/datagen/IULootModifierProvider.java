package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class IULootModifierProvider extends GlobalLootModifierProvider {

    public IULootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    protected void start() {
        addChest("abandoned_mineshaft", BuiltInLootTables.ABANDONED_MINESHAFT, IULootTables.INJECT_ABANDONED_MINESHAFT);
        addChest("desert_pyramid", BuiltInLootTables.DESERT_PYRAMID, IULootTables.INJECT_DESERT_PYRAMID);
        addChest("end_city_treasure", BuiltInLootTables.END_CITY_TREASURE, IULootTables.INJECT_END_CITY_TREASURE);
        addChest("igloo_chest", BuiltInLootTables.IGLOO_CHEST, IULootTables.INJECT_IGLOO_CHEST);
        addChest("jungle_temple", BuiltInLootTables.JUNGLE_TEMPLE, IULootTables.INJECT_JUNGLE_TEMPLE);
        addChest("nether_bridge", BuiltInLootTables.NETHER_BRIDGE, IULootTables.INJECT_NETHER_BRIDGE);
        addChest("simple_dungeon", BuiltInLootTables.SIMPLE_DUNGEON, IULootTables.INJECT_SIMPLE_DUNGEON);
        addChest("spawn_bonus_chest", BuiltInLootTables.SPAWN_BONUS_CHEST, IULootTables.INJECT_SPAWN_BONUS_CHEST);
        addChest("stronghold_corridor", BuiltInLootTables.STRONGHOLD_CORRIDOR, IULootTables.INJECT_STRONGHOLD_CORRIDOR);
        addChest("stronghold_crossing", BuiltInLootTables.STRONGHOLD_CROSSING, IULootTables.INJECT_STRONGHOLD_CROSSING);
        addChest("stronghold_library", BuiltInLootTables.STRONGHOLD_LIBRARY, IULootTables.INJECT_STRONGHOLD_LIBRARY);
        addChest("village_toolsmith", BuiltInLootTables.VILLAGE_TOOLSMITH, IULootTables.INJECT_VILLAGE_TOOLSMITH);
    }

    private void addChest(String name, ResourceKey<LootTable> vanillaTable, ResourceKey<LootTable> injectTable) {
        add(
                "inject_" + name,
                new AddTableLootModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(vanillaTable.location()).build()
                        },
                        injectTable
                )
        );
    }
}