package com.denfop.datagen;

import com.denfop.IUItem;
import com.denfop.items.ItemCraftingElements;
import com.denfop.items.resource.ItemIngots;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class IUChestLoot implements LootTableSubProvider {

    @SuppressWarnings("unused")
    private final HolderLookup.Provider registries;

    public IUChestLoot(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(IULootTables.INJECT_ABANDONED_MINESHAFT, getAbandonedMineshaft());
        consumer.accept(IULootTables.INJECT_DESERT_PYRAMID, getDesertPyramide());
        consumer.accept(IULootTables.INJECT_END_CITY_TREASURE, getAbandonedMineshaft());
        consumer.accept(IULootTables.INJECT_IGLOO_CHEST, getDesertPyramide());
        consumer.accept(IULootTables.INJECT_JUNGLE_TEMPLE, getAbandonedMineshaft());
        consumer.accept(IULootTables.INJECT_NETHER_BRIDGE, getAbandonedMineshaft());
        consumer.accept(IULootTables.INJECT_SIMPLE_DUNGEON, getDungeon());
        consumer.accept(IULootTables.INJECT_SPAWN_BONUS_CHEST, getAbandonedMineshaft());
        consumer.accept(IULootTables.INJECT_STRONGHOLD_CORRIDOR, getDesertPyramide());
        consumer.accept(IULootTables.INJECT_STRONGHOLD_CROSSING, getDesertPyramide());
        consumer.accept(IULootTables.INJECT_STRONGHOLD_LIBRARY, getDesertPyramide());
        consumer.accept(IULootTables.INJECT_VILLAGE_TOOLSMITH, getDesertPyramide());

        consumer.accept(IULootTables.VOLCANO, getVolcano());
    }

    private LootTable.Builder getDungeon() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 3.0F));

        addAllIndustrialIngots(pool, 7, 2, 6);

        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_291_element.getId()), 10, 6, 14));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_275_element.getId()), 4, 1, 2));
        pool.add(EmptyLootItem.emptyItem().setWeight(15));

        return LootTable.lootTable().withPool(pool);
    }

    public LootTable.Builder getVolcano() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 3.0F));

        addAllIndustrialIngots(pool, 7, 2, 6);

        pool.add(addItem(ItemStackHelper.fromData(Items.DIAMOND, 1), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(Items.IRON_INGOT, 1), 8, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(Items.COPPER_INGOT, 1), 8, 2, 6));
        pool.add(addItem(ItemStackHelper.fromData(Items.GOLD_INGOT, 1), 7, 1, 3));
        pool.add(addItem(ItemStackHelper.fromData(Items.LAPIS_LAZULI, 1), 8, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(Items.ENDER_PEARL, 1), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(Items.LEATHER, 1), 6, 1, 6));
        pool.add(addItem(ItemStackHelper.fromData(Items.BLAZE_ROD, 1), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(Items.EMERALD, 1), 6, 1, 2));

        pool.add(addItem(ItemStackHelper.fromData(IUItem.ForgeHammer, 1), 6, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.plast, 1), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.plastic_plate, 1), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 319), 6, 1, 3));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 297), 7, 1, 3));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 286), 6, 1, 3));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 13), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 14), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 290), 6, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 274), 6, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.cutter, 1), 6, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.treetap, 1), 6, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.wrench, 1), 6, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 12), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 15), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.itemiu, 1, 2), 6, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 271), 6, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 280), 6, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 276), 6, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(Items.REDSTONE, 1), 8, 1, 4));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 17), 6, 1, 2));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1, 16), 6, 1, 2));

        pool.add(EmptyLootItem.emptyItem().setWeight(40));

        return LootTable.lootTable().withPool(pool);
    }

    public LootTable.Builder getAbandonedMineshaft() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 3.0F));

        addAllIndustrialIngots(pool, 7, 2, 6);

        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_291_element.getId()), 6, 2, 5));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_290_element.getId()), 7, 5, 10));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_271_element.getId()), 7, 5, 10));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_295_element.getId()), 7, 5, 10));
        pool.add(EmptyLootItem.emptyItem().setWeight(15));

        return LootTable.lootTable().withPool(pool);
    }

    public LootTable.Builder getDesertPyramide() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(1.0F, 3.0F));

        addAllIndustrialIngots(pool, 7, 2, 6);

        pool.add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1, ItemCraftingElements.Types.crafting_291_element.getId()), 6, 2, 5));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.bronze_boots), 3, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.bronze_helmet), 3, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.bronze_chestplate), 3, 1, 1));
        pool.add(addItem(ItemStackHelper.fromData(IUItem.bronze_leggings), 3, 1, 1));
        pool.add(EmptyLootItem.emptyItem().setWeight(15));

        return LootTable.lootTable().withPool(pool);
    }

    private void addAllIndustrialIngots(LootPool.Builder pool, int weight, int min, int max) {
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.mikhail_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.aluminium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.vanadium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.tungsten_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.invar_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.caravky_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.cobalt_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.magnesium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.nickel_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.platinum_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.titanium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.chromium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.spinel_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.electrum_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.silver_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.zinc_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.manganese_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.iridium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.germanium_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.alloy_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.bronze_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.lead_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.steel_ingot, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.osmium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.tantalum, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.cadmium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.arsenic, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.barium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.bismuth, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.gadolinium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.gallium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.hafnium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.yttrium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.molybdenum, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.neodymium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.niobium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.palladium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.polonium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.thallium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.zirconium, weight, min, max));
        pool.add(addIngot(ItemIngots.ItemIngotsTypes.tin_ingot, weight, min, max));
    }

    private LootPoolSingletonContainer.Builder<?> addIngot(ItemIngots.ItemIngotsTypes type, int weight, int min, int max) {
        return addItem(ItemStackHelper.fromData(IUItem.iuingot, 1, type.getId()), weight, min, max);
    }

    public LootPoolSingletonContainer.Builder<?> addItem(ItemStack stack, int weight, int min, int max) {
        return LootItem.lootTableItem(stack.getItem())
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }
}