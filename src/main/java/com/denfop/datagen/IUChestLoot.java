package com.denfop.datagen;

import com.denfop.IUItem;
import com.denfop.items.ItemCraftingElements;
import com.denfop.items.resource.ItemIngots;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.denfop.datagen.IULootTableProvider.VOLCANO_LOOT_TABLE;

public class IUChestLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_124363_) {
        p_124363_.accept(BuiltInLootTables.ABANDONED_MINESHAFT, getAbandonedMineshaft());
        p_124363_.accept(BuiltInLootTables.DESERT_PYRAMID, getDesertPyramide());
        p_124363_.accept(BuiltInLootTables.END_CITY_TREASURE, getAbandonedMineshaft());
        p_124363_.accept(BuiltInLootTables.IGLOO_CHEST, getDesertPyramide());
        p_124363_.accept(BuiltInLootTables.JUNGLE_TEMPLE, getAbandonedMineshaft());
        p_124363_.accept(BuiltInLootTables.NETHER_BRIDGE, getAbandonedMineshaft());
        p_124363_.accept(BuiltInLootTables.SIMPLE_DUNGEON, getDungeon());
        p_124363_.accept(BuiltInLootTables.SPAWN_BONUS_CHEST, getAbandonedMineshaft());
        p_124363_.accept(BuiltInLootTables.STRONGHOLD_CORRIDOR, getDesertPyramide());
        p_124363_.accept(BuiltInLootTables.STRONGHOLD_CROSSING, getDesertPyramide());

        p_124363_.accept(BuiltInLootTables.STRONGHOLD_LIBRARY,  getDesertPyramide());
        p_124363_.accept(BuiltInLootTables.VILLAGE_TOOLSMITH, getDesertPyramide());
        p_124363_.accept(VOLCANO_LOOT_TABLE,getVolcano());
    }

    private LootTable.Builder getDungeon() {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.mikhail_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.aluminium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.vanadium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tungsten_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.invar_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.caravky_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.cobalt_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.magnesium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.nickel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.platinum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.titanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.chromium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.spinel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.electrum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.silver_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.zinc_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.manganese_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.iridium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.germanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.alloy_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bronze_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.lead_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.steel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.osmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tantalum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.cadmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.arsenic.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.barium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bismuth.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.gadolinium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.gallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.hafnium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.yttrium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.molybdenum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.neodymium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.niobium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.palladium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.polonium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.thallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.zirconium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tin_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_291_element.getId()), 10, 6, 14))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_275_element.getId()), 4, 1, 2))
                .add(EmptyLootItem.emptyItem().setWeight(15))

        );
    }
    public LootTable.Builder getVolcano() {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.mikhail_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.aluminium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.vanadium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tungsten_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.invar_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.caravky_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cobalt_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.magnesium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.nickel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.platinum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.titanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.chromium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.spinel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.electrum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.silver_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.zinc_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.manganese_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.iridium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.germanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.alloy_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bronze_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.lead_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.steel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.osmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tantalum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cadmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.arsenic.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.barium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bismuth.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.gadolinium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.gallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.hafnium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.yttrium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.molybdenum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.neodymium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.niobium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.palladium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.polonium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.thallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.zirconium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tin_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(Items.DIAMOND, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.IRON_INGOT, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(Items.COPPER_INGOT, 1), 8, 2, 6))
                .add(addItem(ItemStackHelper.fromData(Items.GOLD_INGOT, 1), 7, 1, 3))
                .add(addItem(ItemStackHelper.fromData(Items.LAPIS_LAZULI, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(Items.ENDER_PEARL, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.LEATHER, 1), 6, 1, 6))
                .add(addItem(ItemStackHelper.fromData(Items.BLAZE_ROD, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(Items.EMERALD, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.ForgeHammer, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.plast, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.plastic_plate, 1), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,319), 6, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,297), 7, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,286), 6, 1, 3))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,13), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,14), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,290), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,274), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.cutter, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.treetap, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.wrench, 1), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,12), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,15), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.itemiu, 1,2), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,271), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,280), 6, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,276), 6, 1, 1))
                .add(addItem(ItemStackHelper.fromData(Items.REDSTONE, 1), 8, 1, 4))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,17), 6, 1, 2))
                .add(addItem(ItemStackHelper.fromData(IUItem.basecircuit, 1,16), 6, 1, 2))
                .add(EmptyLootItem.emptyItem().setWeight(40))

        );
    }
    public LootTable.Builder getAbandonedMineshaft() {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.mikhail_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.aluminium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.vanadium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tungsten_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.invar_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.caravky_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cobalt_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.magnesium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.nickel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.platinum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.titanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.chromium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.spinel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.electrum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.silver_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.zinc_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.manganese_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.iridium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.germanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.alloy_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bronze_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.lead_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.steel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.osmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.tantalum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.cadmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.arsenic.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.barium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bismuth.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.gadolinium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.gallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.hafnium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.yttrium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.molybdenum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.neodymium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.niobium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.palladium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.polonium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.thallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.zirconium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tin_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_291_element.getId()), 6, 2, 5))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_290_element.getId()), 7, 5, 10))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_271_element.getId()), 7, 5, 10))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_295_element.getId()), 7, 5, 10))
                .add(EmptyLootItem.emptyItem().setWeight(15))

        );
    }

    public LootTable.Builder getDesertPyramide() {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.mikhail_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.aluminium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.vanadium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tungsten_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.invar_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.caravky_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.cobalt_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.magnesium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.nickel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.platinum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.titanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.chromium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.spinel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.electrum_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.silver_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.zinc_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.manganese_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.iridium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.germanium_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.alloy_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.bronze_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.lead_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.steel_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.osmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tantalum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.cadmium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.arsenic.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.barium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.bismuth.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.gadolinium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.gallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.hafnium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.yttrium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.molybdenum.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot,1, ItemIngots.ItemIngotsTypes.neodymium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.niobium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.palladium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.polonium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.thallium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.zirconium.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.iuingot, 1,ItemIngots.ItemIngotsTypes.tin_ingot.getId()), 7, 2, 6))
                .add(addItem(ItemStackHelper.fromData(IUItem.crafting_elements, 1,ItemCraftingElements.Types.crafting_291_element.getId()), 6, 2, 5))
                .add(addItem(ItemStackHelper.fromData(IUItem.bronze_boots), 3, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.bronze_helmet), 3, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.bronze_chestplate), 3, 1, 1))
                .add(addItem(ItemStackHelper.fromData(IUItem.bronze_leggings), 3, 1, 1))
                .add(EmptyLootItem.emptyItem().setWeight(15))


        );
    }

    public LootPoolSingletonContainer.Builder<?> addItem(ItemStack stack, int weight, int min, int max) {
        return LootItem.lootTableItem(stack.getItem()).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }
}