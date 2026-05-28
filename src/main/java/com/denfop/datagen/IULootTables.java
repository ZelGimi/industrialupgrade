package com.denfop.datagen;

import com.denfop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public final class IULootTables {

    public static final ResourceKey<LootTable> VOLCANO =
            create("chests/volcano");
    public static final ResourceKey<LootTable> INJECT_ABANDONED_MINESHAFT =
            create("inject/chests/abandoned_mineshaft");
    public static final ResourceKey<LootTable> INJECT_DESERT_PYRAMID =
            create("inject/chests/desert_pyramid");
    public static final ResourceKey<LootTable> INJECT_END_CITY_TREASURE =
            create("inject/chests/end_city_treasure");
    public static final ResourceKey<LootTable> INJECT_IGLOO_CHEST =
            create("inject/chests/igloo_chest");
    public static final ResourceKey<LootTable> INJECT_JUNGLE_TEMPLE =
            create("inject/chests/jungle_temple");
    public static final ResourceKey<LootTable> INJECT_NETHER_BRIDGE =
            create("inject/chests/nether_bridge");
    public static final ResourceKey<LootTable> INJECT_SIMPLE_DUNGEON =
            create("inject/chests/simple_dungeon");
    public static final ResourceKey<LootTable> INJECT_SPAWN_BONUS_CHEST =
            create("inject/chests/spawn_bonus_chest");
    public static final ResourceKey<LootTable> INJECT_STRONGHOLD_CORRIDOR =
            create("inject/chests/stronghold_corridor");
    public static final ResourceKey<LootTable> INJECT_STRONGHOLD_CROSSING =
            create("inject/chests/stronghold_crossing");
    public static final ResourceKey<LootTable> INJECT_STRONGHOLD_LIBRARY =
            create("inject/chests/stronghold_library");
    public static final ResourceKey<LootTable> INJECT_VILLAGE_TOOLSMITH =
            create("inject/chests/village_toolsmith");

    private IULootTables() {
    }

    private static ResourceKey<LootTable> create(String path) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path)
        );
    }
}